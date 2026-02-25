package com.hormaz.studentplanner.ui.task;

import com.hormaz.studentplanner.data.model.Category;
import com.hormaz.studentplanner.data.model.SubTask;
import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.data.repository.CategoryRepository;
import com.hormaz.studentplanner.data.repository.TaskRepository;
import com.hormaz.studentplanner.ui.common.BasePresenter;

import java.util.List;

public class TaskDetailPresenter extends BasePresenter<TaskDetailContract.View>
        implements TaskDetailContract.Presenter {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public TaskDetailPresenter(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void loadTask(String taskId) {
        if (!isViewAttached()) return;
        getView().showLoading();

        taskRepository.getTaskById(taskId, new TaskRepository.TaskCallback<Task>() {
            @Override
            public void onSuccess(Task result) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().showTask(result);
                    if (result.getSubTasks() != null) {
                        getView().showSubTasks(result.getSubTasks());
                    }
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
    }

    @Override
    public void loadCategories() {
        categoryRepository.getAllCategories(new CategoryRepository.CategoryCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                if (isViewAttached()) {
                    getView().showCategories(result);
                }
            }

            @Override
            public void onError(String message) {
                // Silent fail
            }
        });
    }

    @Override
    public void saveTask(Task task) {
        if (!isViewAttached()) return;

        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            getView().setTitleError("Please enter a task title");
            return;
        }

        getView().showLoading();

        if (task.getId() != null && !task.getId().isEmpty()) {
            // Update existing task
            taskRepository.updateTask(task, new TaskRepository.TaskCallback<Task>() {
                @Override
                public void onSuccess(Task result) {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().onTaskSaved();
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
        } else {
            // Create new task
            taskRepository.createTask(task, new TaskRepository.TaskCallback<Task>() {
                @Override
                public void onSuccess(Task result) {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().onTaskSaved();
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
        }
    }

    @Override
    public void deleteTask(String taskId) {
        if (!isViewAttached()) return;
        getView().showLoading();

        taskRepository.deleteTask(taskId, new TaskRepository.TaskCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onTaskDeleted();
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
    }

    @Override
    public void toggleSubTask(String subTaskId, boolean completed) {
        taskRepository.toggleSubTask(subTaskId, completed, new TaskRepository.TaskCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Sub-task updated
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) getView().showError(message);
            }
        });
    }

    @Override
    public void addSubTask(SubTask subTask) {
        taskRepository.addSubTask(subTask, new TaskRepository.TaskCallback<SubTask>() {
            @Override
            public void onSuccess(SubTask result) {
                // Sub-task added, reload task to refresh list
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) getView().showError(message);
            }
        });
    }

    @Override
    public void onDestroy() {
        detachView();
    }
}
