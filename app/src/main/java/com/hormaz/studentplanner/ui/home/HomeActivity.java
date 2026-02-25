package com.hormaz.studentplanner.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.ui.calendar.CalendarActivity;
import com.hormaz.studentplanner.ui.home.adapter.TaskAdapter;
import com.hormaz.studentplanner.ui.settings.SettingsActivity;
import com.hormaz.studentplanner.ui.stats.StatsActivity;
import com.hormaz.studentplanner.ui.task.AddTaskBottomSheet;
import com.hormaz.studentplanner.ui.task.TaskDetailActivity;

import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements HomeContract.View, TaskAdapter.OnTaskClickListener {

    private HomePresenter presenter;
    private TaskAdapter taskAdapter;

    private TextView tvGreeting, tvDate, tvProgressLabel;
    private RecyclerView rvTasks, rvFocusTasks;
    private LinearProgressIndicator progressBar;
    private ProgressBar loadingBar;
    private View emptyState;
    private SwipeRefreshLayout swipeRefresh;
    private ExtendedFloatingActionButton fabAdd;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupEdgeToEdge();
        setupRecyclerView();
        setupBottomNavigation();
        setupListeners();
        initPresenter();
    }

    private void initViews() {
        tvGreeting = findViewById(R.id.tvGreeting);
        tvDate = findViewById(R.id.tvDate);
        tvProgressLabel = findViewById(R.id.tvProgressLabel);
        rvTasks = findViewById(R.id.rvTasks);
        rvFocusTasks = findViewById(R.id.rvFocusTasks);
        progressBar = findViewById(R.id.progressBar);
        loadingBar = findViewById(R.id.loadingBar);
        emptyState = findViewById(R.id.emptyState);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        fabAdd = findViewById(R.id.fabAdd);
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
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(taskAdapter);

        // Swipe to delete
        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder vh,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int pos = viewHolder.getBindingAdapterPosition();
                        Task task = taskAdapter.getTaskAt(pos);
                        if (task != null) {
                            presenter.deleteTask(task.getId(), pos);
                            showDeleteSnackbar(task, pos);
                        }
                    }
                };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(rvTasks);
    }

    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
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
            if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void setupListeners() {
        fabAdd.setOnClickListener(v -> {
            AddTaskBottomSheet sheet = new AddTaskBottomSheet();
            sheet.setOnTaskSavedListener(() -> presenter.loadData());
            sheet.show(getSupportFragmentManager(), "AddTask");
        });

        swipeRefresh.setOnRefreshListener(() -> {
            presenter.syncData();
            presenter.loadData();
        });

        swipeRefresh.setColorSchemeResources(R.color.primary);
    }

    private void initPresenter() {
        App app = App.getInstance();
        String userName = app.getPreferencesManager().getUserName();
        presenter = new HomePresenter(
                app.getTaskRepository(),
                app.getSyncManager(),
                userName != null ? userName : "Student"
        );
        presenter.attachView(this);
        presenter.loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.loadData();
        }
        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }

    // ── HomeContract.View ──

    @Override
    public void showGreeting(String greeting, String name) {
        tvGreeting.setText(greeting + ", " + name + " \uD83D\uDC4B");
    }

    @Override
    public void showDate(String dateText) {
        tvDate.setText(dateText);
    }

    @Override
    public void showFocusTasks(List<Task> focusTasks) {
        if (focusTasks == null || focusTasks.isEmpty()) {
            if (rvFocusTasks != null) rvFocusTasks.setVisibility(View.GONE);
            View focusSection = findViewById(R.id.focusSection);
            if (focusSection != null) focusSection.setVisibility(View.GONE);
        } else {
            View focusSection = findViewById(R.id.focusSection);
            if (focusSection != null) focusSection.setVisibility(View.VISIBLE);
            // Focus tasks adapter (simplified - just uses TaskAdapter)
            if (rvFocusTasks != null) {
                rvFocusTasks.setVisibility(View.VISIBLE);
                TaskAdapter focusAdapter = new TaskAdapter(this, false);
                rvFocusTasks.setLayoutManager(
                        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                rvFocusTasks.setAdapter(focusAdapter);
                focusAdapter.setTasks(focusTasks);
            }
        }
    }

    @Override
    public void showTasks(List<Task> tasks) {
        taskAdapter.setTasks(tasks);
    }

    @Override
    public void showEmptyState() {
        emptyState.setVisibility(View.VISIBLE);
        rvTasks.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyState() {
        emptyState.setVisibility(View.GONE);
        rvTasks.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCompletionProgress(int completed, int total) {
        if (total > 0) {
            int percentage = (int) ((completed * 100f) / total);
            progressBar.setProgress(percentage);
            tvProgressLabel.setText(completed + "/" + total + " tasks done");
        } else {
            progressBar.setProgress(0);
            tvProgressLabel.setText("No tasks yet");
        }
    }

    @Override
    public void onTaskCompleted(int position) {
        presenter.loadData();
    }

    @Override
    public void onTaskDeleted(int position) {
        taskAdapter.removeAt(position);
    }

    @Override
    public void onSyncComplete(boolean success) {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingBar.setVisibility(View.GONE);
        swipeRefresh.setRefreshing(false);
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
        if (isChecked) {
            presenter.completeTask(task.getId(), position);
            showCompleteSnackbar(task, position);
        } else {
            presenter.undoComplete(task.getId(), position);
        }
    }

    private void showCompleteSnackbar(Task task, int position) {
        Snackbar.make(findViewById(android.R.id.content),
                        R.string.task_completed, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> presenter.undoComplete(task.getId(), position))
                .setActionTextColor(getColor(R.color.primary))
                .show();
    }

    private void showDeleteSnackbar(Task task, int position) {
        Snackbar.make(findViewById(android.R.id.content),
                        R.string.task_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> presenter.undoDelete(task.getId(), position))
                .setActionTextColor(getColor(R.color.primary))
                .show();
    }
}
