package com.hormaz.studentplanner.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.BuildConfig;
import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.data.local.prefs.PreferencesManager;
import com.hormaz.studentplanner.data.repository.SyncManager;
import com.hormaz.studentplanner.ui.auth.LoginActivity;
import com.hormaz.studentplanner.ui.calendar.CalendarActivity;
import com.hormaz.studentplanner.ui.home.HomeActivity;
import com.hormaz.studentplanner.ui.stats.StatsActivity;
import com.hormaz.studentplanner.util.DateUtils;
import com.hormaz.studentplanner.util.NetworkUtils;

public class SettingsActivity extends AppCompatActivity {

    private PreferencesManager prefs;

    private MaterialSwitch switchDarkMode, switchNotifications, switchStreakReminder;
    private TextView tvSyncNow, tvLastSynced, tvLogOut, tvVersion, tvUserEmail;
    private ProgressBar loadingBar;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = App.getInstance().getPreferencesManager();

        initViews();
        setupEdgeToEdge();
        setupBottomNavigation();
        loadSettings();
        setupListeners();
    }

    private void initViews() {
        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchNotifications = findViewById(R.id.switchNotifications);
        switchStreakReminder = findViewById(R.id.switchStreakReminder);
        tvSyncNow = findViewById(R.id.tvSyncNow);
        tvLastSynced = findViewById(R.id.tvLastSynced);
        tvLogOut = findViewById(R.id.tvLogOut);
        tvVersion = findViewById(R.id.tvVersion);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        loadingBar = findViewById(R.id.loadingBar);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(),
                    v.getPaddingRight(), insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.nav_settings);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_settings) return true;
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_calendar) {
                startActivity(new Intent(this, CalendarActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_stats) {
                startActivity(new Intent(this, StatsActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void loadSettings() {
        // Use a flag to prevent listener from firing during initialization
        switchDarkMode.setOnCheckedChangeListener(null);
        switchNotifications.setOnCheckedChangeListener(null);
        switchStreakReminder.setOnCheckedChangeListener(null);

        switchDarkMode.setChecked("dark".equals(prefs.getThemeMode()));
        switchNotifications.setChecked(prefs.isNotificationEnabled());
        switchStreakReminder.setChecked(prefs.isStreakReminderEnabled());

        String email = prefs.getUserEmail();
        tvUserEmail.setText(email != null ? email : "Not signed in");

        long lastSync = prefs.getLastSyncTime();
        if (lastSync > 0) {
            tvLastSynced.setText(getString(R.string.last_synced,
                    DateUtils.formatDateTime(DateUtils.nowISO())));
        } else {
            tvLastSynced.setText(R.string.never_synced);
        }

        tvVersion.setText(getString(R.string.version, BuildConfig.VERSION_NAME));
    }

    private void setupListeners() {
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String newMode = isChecked ? "dark" : "system";
            prefs.setThemeMode(newMode);
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES
                             : AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.setNotificationEnabled(isChecked));

        switchStreakReminder.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.setStreakReminderEnabled(isChecked));

        tvSyncNow.setOnClickListener(v -> syncNow());

        tvLogOut.setOnClickListener(v -> confirmLogout());
    }

    private void syncNow() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Snackbar.make(findViewById(android.R.id.content),
                    R.string.error_no_internet, Snackbar.LENGTH_SHORT).show();
            return;
        }

        loadingBar.setVisibility(View.VISIBLE);
        App.getInstance().getSyncManager().syncAll(new SyncManager.SyncCallback() {
            @Override
            public void onComplete(boolean success, String message) {
                runOnUiThread(() -> {
                    loadingBar.setVisibility(View.GONE);
                    if (success) {
                        tvLastSynced.setText(getString(R.string.last_synced, "Just now"));
                        Snackbar.make(findViewById(android.R.id.content),
                                "Sync complete", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content),
                                "Sync failed: " + message, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void confirmLogout() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.log_out)
                .setMessage(R.string.log_out_confirm)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    prefs.clearAuth();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_settings);
    }
}
