package com.hormaz.studentplanner.ui.home;

import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.ui.common.BaseView;

import java.util.List;

public interface HomeContract {

    interface View extends BaseView {
        void showGreeting(String greeting, String name);
        void showDate(String dateText);
        void showFocusTasks(List<Task> focusTasks);
        void showTasks(List<Task> tasks);
        void showEmptyState();
        void hideEmptyState();
        void showCompletionProgress(int completed, int total);
        void onTaskCompleted(int position);
        void onTaskDeleted(int position);
        void onSyncComplete(boolean success);
    }

    interface Presenter {
        void loadData();
        void completeTask(String taskId, int position);
        void deleteTask(String taskId, int position);
        void undoComplete(String taskId, int position);
        void undoDelete(String taskId, int position);
        void syncData();
        void onDestroy();
    }
}
