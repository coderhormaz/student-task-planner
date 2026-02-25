package com.hormaz.studentplanner.ui.task;

import com.hormaz.studentplanner.data.model.Category;
import com.hormaz.studentplanner.data.model.SubTask;
import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.ui.common.BaseView;

import java.util.List;

public interface TaskDetailContract {

    interface View extends BaseView {
        void showTask(Task task);
        void showSubTasks(List<SubTask> subTasks);
        void showCategories(List<Category> categories);
        void onTaskSaved();
        void onTaskDeleted();
        void setTitleError(String error);
    }

    interface Presenter {
        void loadTask(String taskId);
        void loadCategories();
        void saveTask(Task task);
        void deleteTask(String taskId);
        void toggleSubTask(String subTaskId, boolean completed);
        void addSubTask(SubTask subTask);
        void onDestroy();
    }
}
