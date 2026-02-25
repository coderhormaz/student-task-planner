package com.hormaz.studentplanner.ui.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.data.model.Category;
import com.hormaz.studentplanner.data.model.SubTask;
import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.ui.task.adapter.SubTaskAdapter;
import com.hormaz.studentplanner.util.DateUtils;
import com.hormaz.studentplanner.util.ReminderReceiver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskDetailActivity extends AppCompatActivity
        implements TaskDetailContract.View, SubTaskAdapter.OnSubTaskListener {

    private TaskDetailPresenter presenter;
    private SubTaskAdapter subTaskAdapter;

    private MaterialToolbar toolbar;
    private TextInputLayout tilTitle, tilDescription;
    private TextInputEditText etTitle, etDescription;
    private TextView tvDueDate, tvDueTime, tvCategory, tvReminder;
    private ChipGroup chipGroupPriority;
    private Chip chipLow, chipMedium, chipHigh;
    private RecyclerView rvSubTasks;
    private TextInputEditText etNewSubTask;
    private ImageButton btnAddSubTask;
    private ProgressBar loadingBar;

    private Task currentTask;
    private String taskId;
    private boolean isEditMode;
    private int selectedPriority = 1;
    private String selectedCategoryId;
    private String selectedDueDate;
    private String selectedDueTime;
    private String selectedReminder;
    private List<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        initViews();
        setupListeners();
        initPresenter();

        taskId = getIntent().getStringExtra("task_id");
        if (taskId != null && !taskId.isEmpty()) {
            isEditMode = true;
            toolbar.setTitle(R.string.edit_task);
            presenter.loadTask(taskId);
        } else {
            toolbar.setTitle(R.string.add_task);
        }

        presenter.loadCategories();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tilTitle = findViewById(R.id.tilTitle);
        tilDescription = findViewById(R.id.tilDescription);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        tvDueDate = findViewById(R.id.tvDueDate);
        tvDueTime = findViewById(R.id.tvDueTime);
        tvCategory = findViewById(R.id.tvCategory);
        tvReminder = findViewById(R.id.tvReminder);
        chipGroupPriority = findViewById(R.id.chipGroupPriority);
        chipLow = findViewById(R.id.chipLow);
        chipMedium = findViewById(R.id.chipMedium);
        chipHigh = findViewById(R.id.chipHigh);
        rvSubTasks = findViewById(R.id.rvSubTasks);
        etNewSubTask = findViewById(R.id.etNewSubTask);
        btnAddSubTask = findViewById(R.id.btnAddSubTask);
        loadingBar = findViewById(R.id.loadingBar);

        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        toolbar.inflateMenu(R.menu.menu_task_detail);

        subTaskAdapter = new SubTaskAdapter(this);
        rvSubTasks.setLayoutManager(new LinearLayoutManager(this));
        rvSubTasks.setAdapter(subTaskAdapter);
    }

    private void setupListeners() {
        // Priority chips
        chipGroupPriority.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chipHigh)) selectedPriority = 3;
            else if (checkedIds.contains(R.id.chipMedium)) selectedPriority = 2;
            else selectedPriority = 1;
        });

        // Due date picker
        tvDueDate.setOnClickListener(v -> showDatePicker());

        // Due time picker
        tvDueTime.setOnClickListener(v -> showTimePicker());

        // Category selector
        tvCategory.setOnClickListener(v -> showCategoryPicker());

        // Add sub-task
        btnAddSubTask.setOnClickListener(v -> addNewSubTask());

        // Save button in toolbar
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_save) {
                saveTask();
                return true;
            }
            if (id == R.id.action_delete && isEditMode) {
                confirmDelete();
                return true;
            }
            return false;
        });
    }

    private void initPresenter() {
        App app = App.getInstance();
        presenter = new TaskDetailPresenter(
                app.getTaskRepository(),
                app.getCategoryRepository()
        );
        presenter.attachView(this);
    }

    private void showDatePicker() {
        LocalDate now = LocalDate.now();
        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, day) -> {
            LocalDate selected = LocalDate.of(year, month + 1, day);
            selectedDueDate = DateUtils.toISO(selected);
            tvDueDate.setText(DateUtils.formatDate(selectedDueDate));
        }, now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
        picker.show();
    }

    private void showTimePicker() {
        LocalTime now = LocalTime.now();
        TimePickerDialog picker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            LocalTime selected = LocalTime.of(hourOfDay, minute);
            if (selectedDueDate != null) {
                LocalDate date = DateUtils.parseDate(selectedDueDate);
                if (date != null) {
                    LocalDateTime dateTime = LocalDateTime.of(date, selected);
                    selectedDueDate = DateUtils.toISO(dateTime);
                    selectedDueTime = selected.toString();
                    tvDueTime.setText(DateUtils.formatTime(selectedDueDate));
                }
            } else {
                selectedDueTime = selected.toString();
                tvDueTime.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, now.getHour(), now.getMinute(), false);
        picker.show();
    }

    private void showCategoryPicker() {
        if (categories.isEmpty()) return;

        String[] names = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            names[i] = categories.get(i).getName();
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.category)
                .setItems(names, (dialog, which) -> {
                    Category selected = categories.get(which);
                    selectedCategoryId = selected.getId();
                    tvCategory.setText(selected.getName());
                })
                .show();
    }

    private void addNewSubTask() {
        String title = etNewSubTask.getText() != null ?
                etNewSubTask.getText().toString().trim() : "";
        if (title.isEmpty()) return;

        SubTask subTask = new SubTask();
        subTask.setId(UUID.randomUUID().toString());
        subTask.setTitle(title);
        subTask.setCompleted(false);
        subTask.setPosition(subTaskAdapter.getItemCount());

        if (isEditMode && taskId != null) {
            subTask.setTaskId(taskId);
            presenter.addSubTask(subTask);
        }

        subTaskAdapter.addSubTask(subTask);
        etNewSubTask.setText("");
    }

    private void saveTask() {
        String title = etTitle.getText() != null ?
                etTitle.getText().toString().trim() : "";
        String description = etDescription.getText() != null ?
                etDescription.getText().toString().trim() : "";

        if (title.isEmpty()) {
            tilTitle.setError(getString(R.string.error_empty_title));
            return;
        }
        tilTitle.setError(null);

        Task task;
        if (isEditMode && currentTask != null) {
            task = currentTask;
        } else {
            task = new Task();
        }

        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(selectedPriority);
        task.setCategoryId(selectedCategoryId);
        task.setDueDate(selectedDueDate);

        if (!isEditMode) {
            task.setSubTasks(subTaskAdapter.getSubTasks());
        }

        // Schedule reminder if due date is set
        if (selectedDueDate != null && !selectedDueDate.isEmpty()) {
            scheduleReminderIfNeeded(task);
        }

        presenter.saveTask(task);
    }

    private void scheduleReminderIfNeeded(Task task) {
        try {
            if (task.getDueDate() != null) {
                LocalDateTime dueDateTime = LocalDateTime.parse(
                        task.getDueDate(), java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                LocalDateTime reminderTime = dueDateTime.minusHours(1);
                long millis = DateUtils.toMillis(
                        reminderTime.getYear(),
                        reminderTime.getMonthValue() - 1,
                        reminderTime.getDayOfMonth(),
                        reminderTime.getHour(),
                        reminderTime.getMinute()
                );
                if (millis > System.currentTimeMillis()) {
                    ReminderReceiver.scheduleReminder(this,
                            task.getId(), task.getTitle(), millis);
                }
            }
        } catch (Exception ignored) {
            // If we can't parse the date, skip the reminder
        }
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete)
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton(R.string.delete, (dialog, which) ->
                        presenter.deleteTask(taskId))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    // ── TaskDetailContract.View ──

    @Override
    public void showTask(Task task) {
        currentTask = task;
        etTitle.setText(task.getTitle());
        etDescription.setText(task.getDescription());

        selectedPriority = task.getPriority();
        switch (selectedPriority) {
            case 3: chipHigh.setChecked(true); break;
            case 2: chipMedium.setChecked(true); break;
            default: chipLow.setChecked(true); break;
        }

        if (task.getDueDate() != null) {
            selectedDueDate = task.getDueDate();
            tvDueDate.setText(DateUtils.formatDate(task.getDueDate()));
            String time = DateUtils.formatTime(task.getDueDate());
            if (!time.isEmpty()) tvDueTime.setText(time);
        }

        if (task.getCategoryName() != null) {
            tvCategory.setText(task.getCategoryName());
            selectedCategoryId = task.getCategoryId();
        }
    }

    @Override
    public void showSubTasks(List<SubTask> subTasks) {
        subTaskAdapter.setSubTasks(subTasks);
    }

    @Override
    public void showCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public void onTaskSaved() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onTaskDeleted() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setTitleError(String error) {
        tilTitle.setError(error);
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

    // ── SubTaskAdapter.OnSubTaskListener ──

    @Override
    public void onSubTaskChecked(SubTask subTask, boolean checked) {
        presenter.toggleSubTask(subTask.getId(), checked);
    }

    @Override
    public void onSubTaskRemoved(SubTask subTask) {
        // Just removes from adapter visually
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }
}
