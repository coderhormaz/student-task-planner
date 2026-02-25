package com.hormaz.studentplanner.data.repository;

import android.os.Handler;
import android.os.Looper;

import com.hormaz.studentplanner.data.local.db.dao.SubTaskDao;
import com.hormaz.studentplanner.data.local.db.dao.TaskDao;
import com.hormaz.studentplanner.data.local.db.entity.SubTaskEntity;
import com.hormaz.studentplanner.data.local.db.entity.TaskEntity;
import com.hormaz.studentplanner.data.local.prefs.PreferencesManager;
import com.hormaz.studentplanner.data.model.SubTask;
import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.data.remote.api.SupabaseDbApi;
import com.hormaz.studentplanner.data.remote.dto.TaskDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {

    private final TaskDao taskDao;
    private final SubTaskDao subTaskDao;
    private final SupabaseDbApi dbApi;
    private final PreferencesManager prefs;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public interface TaskCallback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    public TaskRepository(TaskDao taskDao, SubTaskDao subTaskDao,
                          SupabaseDbApi dbApi, PreferencesManager prefs) {
        this.taskDao = taskDao;
        this.subTaskDao = subTaskDao;
        this.dbApi = dbApi;
        this.prefs = prefs;
        this.executor = Executors.newFixedThreadPool(4);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void getAllTasks(TaskCallback<List<Task>> callback) {
        executor.execute(() -> {
            try {
                String userId = prefs.getUserId();
                List<TaskEntity> entities = taskDao.getAllTasks(userId);
                List<Task> tasks = mapEntitiesToTasks(entities);
                mainHandler.post(() -> callback.onSuccess(tasks));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void getTodaysTasks(TaskCallback<List<Task>> callback) {
        executor.execute(() -> {
            try {
                String userId = prefs.getUserId();
                List<TaskEntity> entities = taskDao.getTodaysTasks(userId);
                List<Task> tasks = mapEntitiesToTasks(entities);
                mainHandler.post(() -> callback.onSuccess(tasks));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void getFocusTasks(TaskCallback<List<Task>> callback) {
        executor.execute(() -> {
            try {
                String userId = prefs.getUserId();
                List<TaskEntity> entities = taskDao.getFocusTasks(userId);
                List<Task> tasks = mapEntitiesToTasks(entities);
                mainHandler.post(() -> callback.onSuccess(tasks));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void getTasksByDate(String date, TaskCallback<List<Task>> callback) {
        executor.execute(() -> {
            try {
                String userId = prefs.getUserId();
                List<TaskEntity> entities = taskDao.getTasksByDate(userId, date);
                List<Task> tasks = mapEntitiesToTasks(entities);
                mainHandler.post(() -> callback.onSuccess(tasks));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void getTasksByDateRange(String startDate, String endDate,
                                     TaskCallback<List<Task>> callback) {
        executor.execute(() -> {
            try {
                String userId = prefs.getUserId();
                List<TaskEntity> entities = taskDao.getTasksByDateRange(userId, startDate, endDate);
                List<Task> tasks = mapEntitiesToTasks(entities);
                mainHandler.post(() -> callback.onSuccess(tasks));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void getTasksByCategory(String categoryId, TaskCallback<List<Task>> callback) {
        executor.execute(() -> {
            try {
                String userId = prefs.getUserId();
                List<TaskEntity> entities = taskDao.getTasksByCategory(userId, categoryId);
                List<Task> tasks = mapEntitiesToTasks(entities);
                mainHandler.post(() -> callback.onSuccess(tasks));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void getTaskById(String taskId, TaskCallback<Task> callback) {
        executor.execute(() -> {
            try {
                TaskEntity entity = taskDao.getTaskById(taskId);
                if (entity != null) {
                    Task task = mapEntityToTask(entity);
                    List<SubTaskEntity> subTaskEntities = subTaskDao.getSubTasksForTask(taskId);
                    task.setSubTasks(mapSubTaskEntities(subTaskEntities));
                    mainHandler.post(() -> callback.onSuccess(task));
                } else {
                    mainHandler.post(() -> callback.onError("Task not found"));
                }
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void createTask(Task task, TaskCallback<Task> callback) {
        executor.execute(() -> {
            try {
                if (task.getId() == null || task.getId().isEmpty()) {
                    task.setId(UUID.randomUUID().toString());
                }
                task.setUserId(prefs.getUserId());

                String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                task.setCreatedAt(now);
                task.setUpdatedAt(now);

                TaskEntity entity = mapTaskToEntity(task);
                taskDao.insert(entity);

                // Insert sub-tasks if any
                if (task.getSubTasks() != null) {
                    for (SubTask st : task.getSubTasks()) {
                        if (st.getId() == null) st.setId(UUID.randomUUID().toString());
                        st.setTaskId(task.getId());
                        subTaskDao.insert(mapSubTaskToEntity(st));
                    }
                }

                mainHandler.post(() -> callback.onSuccess(task));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void updateTask(Task task, TaskCallback<Task> callback) {
        executor.execute(() -> {
            try {
                String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                task.setUpdatedAt(now);

                TaskEntity entity = mapTaskToEntity(task);
                entity.setSynced(false);
                entity.setLocalUpdatedAt(System.currentTimeMillis());
                taskDao.update(entity);

                mainHandler.post(() -> callback.onSuccess(task));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void completeTask(String taskId, TaskCallback<Void> callback) {
        executor.execute(() -> {
            try {
                String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                taskDao.completeTask(taskId, now, System.currentTimeMillis());
                mainHandler.post(() -> callback.onSuccess(null));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void deleteTask(String taskId, TaskCallback<Void> callback) {
        executor.execute(() -> {
            try {
                taskDao.softDelete(taskId, System.currentTimeMillis());
                mainHandler.post(() -> callback.onSuccess(null));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void getCompletionStats(TaskCallback<int[]> callback) {
        executor.execute(() -> {
            try {
                String userId = prefs.getUserId();
                int completed = taskDao.getCompletedCount(userId);
                int total = taskDao.getTotalCount(userId);
                mainHandler.post(() -> callback.onSuccess(new int[]{completed, total}));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void getTaskDatesInRange(String startDate, String endDate,
                                     TaskCallback<List<String>> callback) {
        executor.execute(() -> {
            try {
                String userId = prefs.getUserId();
                List<String> dates = taskDao.getTaskDatesInRange(userId, startDate, endDate);
                mainHandler.post(() -> callback.onSuccess(dates));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void getCompletedCountByDate(String date, TaskCallback<Integer> callback) {
        executor.execute(() -> {
            try {
                String userId = prefs.getUserId();
                int count = taskDao.getCompletedCountByDate(userId, date);
                mainHandler.post(() -> callback.onSuccess(count));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    // ── Sub Tasks ──

    public void addSubTask(SubTask subTask, TaskCallback<SubTask> callback) {
        executor.execute(() -> {
            try {
                if (subTask.getId() == null) subTask.setId(UUID.randomUUID().toString());
                subTaskDao.insert(mapSubTaskToEntity(subTask));
                mainHandler.post(() -> callback.onSuccess(subTask));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void toggleSubTask(String subTaskId, boolean completed, TaskCallback<Void> callback) {
        executor.execute(() -> {
            try {
                subTaskDao.toggleComplete(subTaskId, completed, System.currentTimeMillis());
                mainHandler.post(() -> callback.onSuccess(null));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    // ── Sync Support ──

    public TaskDao getTaskDao() { return taskDao; }
    public SubTaskDao getSubTaskDao() { return subTaskDao; }
    public SupabaseDbApi getDbApi() { return dbApi; }
    public ExecutorService getExecutor() { return executor; }

    // ── Mappers ──

    private Task mapEntityToTask(TaskEntity entity) {
        Task task = new Task();
        task.setId(entity.getId());
        task.setUserId(entity.getUserId());
        task.setCategoryId(entity.getCategoryId());
        task.setTitle(entity.getTitle());
        task.setDescription(entity.getDescription());
        task.setDueDate(entity.getDueDate());
        task.setReminderAt(entity.getReminderAt());
        task.setPriority(entity.getPriority());
        task.setStatus(entity.getStatus());
        task.setFocus(entity.isFocus());
        task.setCompletedAt(entity.getCompletedAt());
        task.setPosition(entity.getPosition());
        task.setCreatedAt(entity.getCreatedAt());
        task.setUpdatedAt(entity.getUpdatedAt());
        return task;
    }

    private List<Task> mapEntitiesToTasks(List<TaskEntity> entities) {
        List<Task> tasks = new ArrayList<>();
        for (TaskEntity entity : entities) {
            tasks.add(mapEntityToTask(entity));
        }
        return tasks;
    }

    private TaskEntity mapTaskToEntity(Task task) {
        TaskEntity entity = new TaskEntity(task.getId(), task.getUserId(), task.getTitle());
        entity.setCategoryId(task.getCategoryId());
        entity.setDescription(task.getDescription());
        entity.setDueDate(task.getDueDate());
        entity.setReminderAt(task.getReminderAt());
        entity.setPriority(task.getPriority());
        entity.setStatus(task.getStatus());
        entity.setFocus(task.isFocus());
        entity.setCompletedAt(task.getCompletedAt());
        entity.setPosition(task.getPosition());
        entity.setCreatedAt(task.getCreatedAt());
        entity.setUpdatedAt(task.getUpdatedAt());
        entity.setSynced(false);
        entity.setLocalUpdatedAt(System.currentTimeMillis());
        return entity;
    }

    private List<SubTask> mapSubTaskEntities(List<SubTaskEntity> entities) {
        List<SubTask> subTasks = new ArrayList<>();
        for (SubTaskEntity entity : entities) {
            SubTask st = new SubTask();
            st.setId(entity.getId());
            st.setTaskId(entity.getTaskId());
            st.setTitle(entity.getTitle());
            st.setCompleted(entity.isCompleted());
            st.setPosition(entity.getPosition());
            subTasks.add(st);
        }
        return subTasks;
    }

    private SubTaskEntity mapSubTaskToEntity(SubTask subTask) {
        SubTaskEntity entity = new SubTaskEntity(
                subTask.getId(), subTask.getTaskId(), subTask.getTitle()
        );
        entity.setCompleted(subTask.isCompleted());
        entity.setPosition(subTask.getPosition());
        return entity;
    }

    public TaskDto mapEntityToDto(TaskEntity entity) {
        TaskDto dto = new TaskDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setCategoryId(entity.getCategoryId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setDueDate(entity.getDueDate());
        dto.setReminderAt(entity.getReminderAt());
        dto.setPriority(entity.getPriority());
        dto.setStatus(entity.getStatus());
        dto.setFocus(entity.isFocus());
        dto.setCompletedAt(entity.getCompletedAt());
        dto.setPosition(entity.getPosition());
        dto.setDeleted(entity.isDeleted());
        return dto;
    }

    public TaskEntity mapDtoToEntity(TaskDto dto) {
        TaskEntity entity = new TaskEntity(dto.getId(), dto.getUserId(), dto.getTitle());
        entity.setCategoryId(dto.getCategoryId());
        entity.setDescription(dto.getDescription());
        entity.setDueDate(dto.getDueDate());
        entity.setReminderAt(dto.getReminderAt());
        entity.setPriority(dto.getPriority());
        entity.setStatus(dto.getStatus());
        entity.setFocus(dto.isFocus());
        entity.setCompletedAt(dto.getCompletedAt());
        entity.setPosition(dto.getPosition());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setDeleted(dto.isDeleted());
        entity.setSynced(true);
        entity.setLocalUpdatedAt(System.currentTimeMillis());
        return entity;
    }
}
