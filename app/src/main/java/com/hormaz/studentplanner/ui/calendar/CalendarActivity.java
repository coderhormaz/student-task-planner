package com.hormaz.studentplanner.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.ui.home.HomeActivity;
import com.hormaz.studentplanner.ui.home.adapter.TaskAdapter;
import com.hormaz.studentplanner.ui.settings.SettingsActivity;
import com.hormaz.studentplanner.ui.stats.StatsActivity;
import com.hormaz.studentplanner.ui.task.TaskDetailActivity;
import com.hormaz.studentplanner.util.DateUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarActivity extends AppCompatActivity
        implements CalendarContract.View, TaskAdapter.OnTaskClickListener {

    private CalendarPresenter presenter;
    private TaskAdapter taskAdapter;

    private android.widget.CalendarView calendarView;
    private RecyclerView rvDayTasks;
    private TextView tvSelectedDate, tvNoTasks;
    private ProgressBar loadingBar;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initViews();
        setupEdgeToEdge();
        setupRecyclerView();
        setupCalendar();
        setupBottomNavigation();
        initPresenter();
    }

    private void initViews() {
        calendarView = findViewById(R.id.calendarView);
        rvDayTasks = findViewById(R.id.rvDayTasks);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvNoTasks = findViewById(R.id.tvNoTasks);
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

    private void setupRecyclerView() {
        taskAdapter = new TaskAdapter(this, false);
        rvDayTasks.setLayoutManager(new LinearLayoutManager(this));
        rvDayTasks.setAdapter(taskAdapter);
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            LocalDate selected = LocalDate.of(year, month + 1, dayOfMonth);
            String dateStr = DateUtils.toISO(selected);
            presenter.loadTasksForDate(dateStr);
        });

        // Load today's tasks by default
        String today = DateUtils.getTodayISO();
        tvSelectedDate.setText(DateUtils.getRelativeDate(today));
    }

    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.nav_calendar);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_calendar) return true;
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_stats) {
                startActivity(new Intent(this, StatsActivity.class));
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

    private void initPresenter() {
        presenter = new CalendarPresenter(App.getInstance().getTaskRepository());
        presenter.attachView(this);

        // Load today's tasks
        presenter.loadTasksForDate(DateUtils.getTodayISO());

        // Load month highlights
        YearMonth current = YearMonth.now();
        presenter.loadMonthHighlights(current.format(DateTimeFormatter.ofPattern("yyyy-MM")));
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_calendar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }

    // ── CalendarContract.View ──

    @Override
    public void showTasksForDate(List<Task> tasks, String dateLabel) {
        tvSelectedDate.setText(dateLabel);
        tvNoTasks.setVisibility(View.GONE);
        rvDayTasks.setVisibility(View.VISIBLE);
        taskAdapter.setTasks(tasks);
    }

    @Override
    public void highlightDates(List<String> dates) {
        // CalendarView doesn't support dot highlights natively
        // The dates with tasks are available for reference
    }

    @Override
    public void showEmptyDate(String dateLabel) {
        tvSelectedDate.setText(dateLabel);
        tvNoTasks.setVisibility(View.VISIBLE);
        rvDayTasks.setVisibility(View.GONE);
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

    // ── TaskAdapter.OnTaskClickListener ──

    @Override
    public void onTaskClick(Task task, int position) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("task_id", task.getId());
        startActivity(intent);
    }

    @Override
    public void onTaskChecked(Task task, int position, boolean isChecked) {
        // Handled via refresh
        if (isChecked) {
            App.getInstance().getTaskRepository().completeTask(task.getId(),
                    new com.hormaz.studentplanner.data.repository.TaskRepository.TaskCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            // Refresh
                        }

                        @Override
                        public void onError(String message) {}
                    });
        }
    }
}
