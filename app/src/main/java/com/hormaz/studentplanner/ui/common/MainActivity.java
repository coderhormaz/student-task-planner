package com.hormaz.studentplanner.ui.common;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.ui.calendar.CalendarActivity;
import com.hormaz.studentplanner.ui.home.HomeActivity;
import com.hormaz.studentplanner.ui.settings.SettingsActivity;
import com.hormaz.studentplanner.ui.stats.StatsActivity;
import com.hormaz.studentplanner.util.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirect directly to HomeActivity and set up bottom nav from there
        startActivity(new Intent(this, HomeActivity.class));
        finish();

        // Trigger background sync if network available
        if (NetworkUtils.isNetworkAvailable(this)) {
            App.getInstance().getSyncManager().syncAll(null);
        }
    }
}
