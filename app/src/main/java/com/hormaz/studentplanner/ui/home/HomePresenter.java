package com.hormaz.studentplanner.ui.home;

import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.data.repository.SyncManager;
import com.hormaz.studentplanner.data.repository.TaskRepository;
import com.hormaz.studentplanner.ui.common.BasePresenter;
import com.hormaz.studentplanner.util.DateUtils;

import java.util.List;

public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {

    private final TaskRepository taskRepository;
    private final SyncManager syncManager;
    private final String userName;

    public HomePresenter(TaskRepository taskRepository, SyncManager syncManager, String userName) {
        this.taskRepository = taskRepository;
        this.syncManager = syncManager;
        this.userName = userName;
    }

    @Override
    public void loadData() {
        if (!isViewAttached()) return;

        // Set greeting and date
        getView().showGreeting(DateUtils.getGreeting(), userName);
        getView().showDate(DateUtils.getTodayDisplay());

        // Load focus tasks
        taskRepository.getFocusTasks(new TaskRepository.TaskCallback<List<Task>>() {
            @Override
            public void onSuccess(List<Task> result) {
                if (isViewAttached()) {
                    getView().showFocusTasks(result);
                }
            }

            @Override
            public void onError(String message) {
                // Silent fail for focus tasks
            }
        });

        // Load today's tasks
        getView().showLoading();
        taskRepository.getTodaysTasks(new TaskRepository.TaskCallback<List<Task>>() {
            @Override
            public void onSuccess(List<Task> result) {
                if (!isViewAttached()) return;
                getView().hideLoading();

                if (result.isEmpty()) {
                    // If no tasks for today, load all pending tasks
                    loadAllPendingTasks();
                } else {
                    getView().showTasks(result);
                    getView().hideEmptyState();
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().showError(message);
                }
            }
        });

        // Load completion stats
        taskRepository.getCompletionStats(new TaskRepository.TaskCallback<int[]>() {
            @Override
            public void onSuccess(int[] result) {
                if (isViewAttached()) {
                    getView().showCompletionProgress(result[0], result[1]);
                }
            }

            @Override
            public void onError(String message) {
                // Silent fail
            }
        });
    }

    private void loadAllPendingTasks() {
        taskRepository.getAllTasks(new TaskRepository.TaskCallback<List<Task>>() {
            @Override
            public void onSuccess(List<Task> result) {
                if (!isViewAttached()) return;
                if (result.isEmpty()) {
                    getView().showEmptyState();
                } else {
                    getView().showTasks(result);
                    getView().hideEmptyState();
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().showError(message);
                }
            }
        });
    }

    @Override
    public void completeTask(String taskId, int position) {
        taskRepository.completeTask(taskId, new TaskRepository.TaskCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                if (isViewAttached()) {
                    getView().onTaskCompleted(position);
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().showError(message);
                }
            }
        });
    }

    @Override
    public void deleteTask(String taskId, int position) {
        taskRepository.deleteTask(taskId, new TaskRepository.TaskCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                if (isViewAttached()) {
                    getView().onTaskDeleted(position);
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().showError(message);
                }
            }
        });
    }

    @Override
    public void undoComplete(String taskId, int position) {
        // Re-mark as pending
        taskRepository.getTaskById(taskId, new TaskRepository.TaskCallback<Task>() {
            @Override
            public void onSuccess(Task result) {
                result.setStatus("pending");
                result.setCompletedAt(null);
                taskRepository.updateTask(result, new TaskRepository.TaskCallback<Task>() {
                    @Override
                    public void onSuccess(Task result) {
                        loadData();
                    }

                    @Override
                    public void onError(String message) {}
                });
            }

            @Override
            public void onError(String message) {}
        });
    }

    @Override
    public void undoDelete(String taskId, int position) {
        taskRepository.getTaskById(taskId, new TaskRepository.TaskCallback<Task>() {
            @Override
            public void onSuccess(Task result) {
                // Task was soft-deleted, re-create it
                taskRepository.createTask(result, new TaskRepository.TaskCallback<Task>() {
                    @Override
                    public void onSuccess(Task result) {
                        loadData();
                    }

                    @Override
                    public void onError(String message) {}
                });
            }

            @Override
            public void onError(String message) {
                // Load data anyway to refresh
                loadData();
            }
        });
    }

    @Override
    public void syncData() {
        syncManager.syncAll(new SyncManager.SyncCallback() {
            @Override
            public void onComplete(boolean success, String message) {
                if (isViewAttached()) {
                    getView().onSyncComplete(success);
                    if (success) loadData();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        detachView();
    }
}
