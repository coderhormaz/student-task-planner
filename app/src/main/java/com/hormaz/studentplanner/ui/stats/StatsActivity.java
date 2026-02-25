package com.hormaz.studentplanner.ui.stats;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.ui.calendar.CalendarActivity;
import com.hormaz.studentplanner.ui.home.HomeActivity;
import com.hormaz.studentplanner.ui.settings.SettingsActivity;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatsActivity extends AppCompatActivity implements StatsContract.View {

    private StatsPresenter presenter;

    private CircularProgressIndicator circularProgress;
    private TextView tvCompletionRate, tvCompletedCount, tvTotalCount;
    private TextView tvStreakCount, tvMotivation;
    private BarChart barChart;
    private ProgressBar loadingBar;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        initViews();
        setupEdgeToEdge();
        setupBottomNavigation();
        setupChart();
        initPresenter();
    }

    private void initViews() {
        circularProgress = findViewById(R.id.circularProgress);
        tvCompletionRate = findViewById(R.id.tvCompletionRate);
        tvCompletedCount = findViewById(R.id.tvCompletedCount);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvStreakCount = findViewById(R.id.tvStreakCount);
        tvMotivation = findViewById(R.id.tvMotivation);
        barChart = findViewById(R.id.barChart);
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
        bottomNav.setSelectedItemId(R.id.nav_stats);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_stats) return true;
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
            if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void setupChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setFitBars(true);
        barChart.getLegend().setEnabled(false);
        barChart.setTouchEnabled(false);

        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisLeft().setGranularity(1f);
        barChart.getAxisRight().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
    }

    private void initPresenter() {
        App app = App.getInstance();
        presenter = new StatsPresenter(app.getTaskRepository(), app.getPreferencesManager());
        presenter.attachView(this);
        presenter.loadStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_stats);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }

    // ── StatsContract.View ──

    @Override
    public void showCompletionRate(int completed, int total) {
        int rate = total > 0 ? (int) ((completed * 100f) / total) : 0;
        circularProgress.setProgress(rate);
        tvCompletionRate.setText(rate + "%");
        tvCompletedCount.setText(String.valueOf(completed));
        tvTotalCount.setText("of " + total + " tasks");
    }

    @Override
    public void showStreak(int days) {
        tvStreakCount.setText(days + " days");
    }

    @Override
    public void showWeeklyData(int[] dailyCounts) {
        List<BarEntry> entries = new ArrayList<>();
        String[] labels = new String[7];

        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, dailyCounts[i]));
            LocalDate date = LocalDate.now().minusDays(6 - i);
            labels[i] = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Tasks Completed");
        dataSet.setColor(getColor(R.color.primary));
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);
        barChart.setData(barData);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.invalidate();
    }

    @Override
    public void showCategoryBreakdown(Map<String, Integer> breakdown) {
        // Could use PieChart here — using text summary for simplicity
    }

    @Override
    public void showMotivationalMessage(String message) {
        tvMotivation.setText(message);
    }

    @Override
    public void showLoading() {
        loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(findViewById(android.R.id.content),
                message != null ? message : getString(R.string.error_generic),
                Snackbar.LENGTH_SHORT).show();
    }
}
