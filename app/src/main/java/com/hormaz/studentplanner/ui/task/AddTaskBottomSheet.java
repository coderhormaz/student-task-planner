package com.hormaz.studentplanner.ui.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.data.model.Category;
import com.hormaz.studentplanner.data.model.SubTask;
import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.data.repository.CategoryRepository;
import com.hormaz.studentplanner.data.repository.TaskRepository;
import com.hormaz.studentplanner.ui.task.adapter.SubTaskAdapter;
import com.hormaz.studentplanner.util.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddTaskBottomSheet extends BottomSheetDialogFragment
        implements SubTaskAdapter.OnSubTaskListener {

    private TextInputLayout tilTitle;
    private TextInputEditText etTitle, etDescription, etNewSubTask;
    private com.google.android.material.button.MaterialButton tvDueDate, tvDueTime, tvCategory;
    private ChipGroup chipGroupPriority;
    private Chip chipLow, chipMedium, chipHigh;
    private RecyclerView rvSubTasks;
    private ImageButton btnAddSubTask;
    private com.google.android.material.button.MaterialButton btnSave;
    private SubTaskAdapter subTaskAdapter;

    private int selectedPriority = 1;
    private String selectedCategoryId;
    private String selectedDueDate;
    private List<Category> categories = new ArrayList<>();
    private OnTaskSavedListener onTaskSavedListener;

    public interface OnTaskSavedListener {
        void onTaskSaved();
    }

    public void setOnTaskSavedListener(OnTaskSavedListener listener) {
        this.onTaskSavedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupListeners();
        loadCategories();
    }

    private void initViews(View view) {
        tilTitle = view.findViewById(R.id.tilTitle);
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        tvDueDate = view.findViewById(R.id.btnDueDate);
        tvDueTime = view.findViewById(R.id.btnDueTime);
        tvCategory = view.findViewById(R.id.btnCategory);
        chipGroupPriority = view.findViewById(R.id.chipGroupPriority);
        chipLow = view.findViewById(R.id.chipLow);
        chipMedium = view.findViewById(R.id.chipMedium);
        chipHigh = view.findViewById(R.id.chipHigh);
        rvSubTasks = view.findViewById(R.id.rvSubTasks);
        etNewSubTask = view.findViewById(R.id.etNewSubTask);
        btnAddSubTask = view.findViewById(R.id.btnAddSubTask);
        btnSave = view.findViewById(R.id.btnSave);

        subTaskAdapter = new SubTaskAdapter(this);
        rvSubTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSubTasks.setAdapter(subTaskAdapter);
    }

    private void setupListeners() {
        chipGroupPriority.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chipHigh)) selectedPriority = 3;
            else if (checkedIds.contains(R.id.chipMedium)) selectedPriority = 2;
            else selectedPriority = 1;
        });

        tvDueDate.setOnClickListener(v -> {
            LocalDate now = LocalDate.now();
            new DatePickerDialog(requireContext(), (view, year, month, day) -> {
                LocalDate selected = LocalDate.of(year, month + 1, day);
                selectedDueDate = DateUtils.toISO(selected);
                tvDueDate.setText(DateUtils.formatDate(selectedDueDate));
            }, now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth()).show();
        });

        tvDueTime.setOnClickListener(v -> {
            LocalTime now = LocalTime.now();
            new TimePickerDialog(requireContext(), (view, hour, minute) -> {
                if (selectedDueDate != null) {
                    LocalDate date = DateUtils.parseDate(selectedDueDate);
                    if (date != null) {
                        LocalDateTime dt = LocalDateTime.of(date, LocalTime.of(hour, minute));
                        selectedDueDate = DateUtils.toISO(dt);
                    }
                }
                tvDueTime.setText(String.format("%02d:%02d", hour, minute));
            }, now.getHour(), now.getMinute(), false).show();
        });

        tvCategory.setOnClickListener(v -> {
            if (categories.isEmpty()) return;
            String[] names = new String[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
                names[i] = categories.get(i).getName();
            }
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.category)
                    .setItems(names, (dialog, which) -> {
                        selectedCategoryId = categories.get(which).getId();
                        tvCategory.setText(categories.get(which).getName());
                    })
                    .show();
        });

        btnAddSubTask.setOnClickListener(v -> {
            String title = etNewSubTask.getText() != null ?
                    etNewSubTask.getText().toString().trim() : "";
            if (title.isEmpty()) return;

            SubTask st = new SubTask();
            st.setId(UUID.randomUUID().toString());
            st.setTitle(title);
            st.setPosition(subTaskAdapter.getItemCount());
            subTaskAdapter.addSubTask(st);
            etNewSubTask.setText("");
        });

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void loadCategories() {
        App.getInstance().getCategoryRepository().getAllCategories(
                new CategoryRepository.CategoryCallback<List<Category>>() {
                    @Override
                    public void onSuccess(List<Category> result) {
                        categories = result;
                    }

                    @Override
                    public void onError(String message) {
                        // Silent fail
                    }
                });
    }

    private void saveTask() {
        String title = etTitle.getText() != null ?
                etTitle.getText().toString().trim() : "";
        if (title.isEmpty()) {
            tilTitle.setError(getString(R.string.error_empty_title));
            return;
        }
        tilTitle.setError(null);

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(etDescription.getText() != null ?
                etDescription.getText().toString().trim() : "");
        task.setPriority(selectedPriority);
        task.setCategoryId(selectedCategoryId);
        task.setDueDate(selectedDueDate);
        task.setSubTasks(subTaskAdapter.getSubTasks());

        App.getInstance().getTaskRepository().createTask(task,
                new TaskRepository.TaskCallback<Task>() {
                    @Override
                    public void onSuccess(Task result) {
                        if (onTaskSavedListener != null) onTaskSavedListener.onTaskSaved();
                        dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        // Show error
                    }
                });
    }

    // ── SubTaskAdapter.OnSubTaskListener ──

    @Override
    public void onSubTaskChecked(SubTask subTask, boolean checked) {
        subTask.setCompleted(checked);
    }

    @Override
    public void onSubTaskRemoved(SubTask subTask) {
        // Already removed from adapter
    }
}
