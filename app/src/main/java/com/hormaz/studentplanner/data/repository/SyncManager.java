package com.hormaz.studentplanner.data.repository;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hormaz.studentplanner.data.local.db.entity.CategoryEntity;
import com.hormaz.studentplanner.data.local.db.entity.TaskEntity;
import com.hormaz.studentplanner.data.local.prefs.PreferencesManager;
import com.hormaz.studentplanner.data.remote.dto.CategoryDto;
import com.hormaz.studentplanner.data.remote.dto.TaskDto;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class SyncManager {

    private static final String TAG = "SyncManager";

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final PreferencesManager prefs;
    private final ExecutorService executor;
    private final Handler mainHandler;
    private boolean isSyncing = false;

    public interface SyncCallback {
        void onComplete(boolean success, String message);
    }

    public SyncManager(TaskRepository taskRepository, CategoryRepository categoryRepository,
                       PreferencesManager prefs) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.prefs = prefs;
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void syncAll(SyncCallback callback) {
        if (isSyncing) {
            if (callback != null) {
                callback.onComplete(false, "Sync already in progress");
            }
            return;
        }

        if (!prefs.isLoggedIn()) {
            if (callback != null) {
                callback.onComplete(false, "Not logged in");
            }
            return;
        }

        isSyncing = true;

        executor.execute(() -> {
            try {
                // Push unsynced local changes
                pushUnsyncedTasks();
                pushUnsyncedCategories();

                // Pull remote changes
                pullRemoteTasks();
                pullRemoteCategories();

                // Update last sync time
                prefs.setLastSyncTime(System.currentTimeMillis());

                isSyncing = false;
                mainHandler.post(() -> {
                    if (callback != null) callback.onComplete(true, "Sync complete");
                });
            } catch (Exception e) {
                Log.e(TAG, "Sync failed", e);
                isSyncing = false;
                mainHandler.post(() -> {
                    if (callback != null) callback.onComplete(false, e.getMessage());
                });
            }
        });
    }

    private void pushUnsyncedTasks() {
        try {
            List<TaskEntity> unsyncedTasks = taskRepository.getTaskDao().getUnsyncedTasks();
            for (TaskEntity entity : unsyncedTasks) {
                TaskDto dto = taskRepository.mapEntityToDto(entity);
                Response<Void> response = taskRepository.getDbApi()
                        .upsertTask(dto, "resolution=merge-duplicates,return=minimal")
                        .execute();
                if (response.isSuccessful()) {
                    taskRepository.getTaskDao().markSynced(entity.getId());
                } else {
                    Log.w(TAG, "Failed to push task: " + entity.getId()
                            + " status: " + response.code());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error pushing tasks", e);
        }
    }

    private void pushUnsyncedCategories() {
        try {
            List<CategoryEntity> unsyncedCategories =
                    categoryRepository.getCategoryDao().getUnsyncedCategories();
            for (CategoryEntity entity : unsyncedCategories) {
                CategoryDto dto = categoryRepository.mapEntityToDto(entity);
                Response<Void> response = categoryRepository.getDbApi()
                        .upsertCategory(dto, "resolution=merge-duplicates,return=minimal")
                        .execute();
                if (response.isSuccessful()) {
                    categoryRepository.getCategoryDao().markSynced(entity.getId());
                } else {
                    Log.w(TAG, "Failed to push category: " + entity.getId());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error pushing categories", e);
        }
    }

    private void pullRemoteTasks() {
        try {
            String userId = prefs.getUserId();
            long lastSync = prefs.getLastSyncTime();
            String updatedAtFilter = lastSync > 0
                    ? "gt." + new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .format(new java.util.Date(lastSync))
                    : null;

            Response<List<TaskDto>> response = taskRepository.getDbApi()
                    .getTasks("eq." + userId, "eq.false", "*",
                            "updated_at.desc", updatedAtFilter)
                    .execute();

            if (response.isSuccessful() && response.body() != null) {
                for (TaskDto dto : response.body()) {
                    TaskEntity remoteEntity = taskRepository.mapDtoToEntity(dto);
                    TaskEntity localEntity = taskRepository.getTaskDao().getTaskById(dto.getId());

                    if (localEntity == null) {
                        // New remote task — insert locally
                        taskRepository.getTaskDao().insert(remoteEntity);
                    } else if (localEntity.isSynced()) {
                        // Local is synced, remote has update — overwrite
                        taskRepository.getTaskDao().insert(remoteEntity);
                    }
                    // If local is unsynced (has pending changes), keep local (push will handle it)
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error pulling tasks", e);
        }
    }

    private void pullRemoteCategories() {
        try {
            String userId = prefs.getUserId();

            Response<List<CategoryDto>> response = categoryRepository.getDbApi()
                    .getCategories("eq." + userId, "eq.false", "*", "name.asc")
                    .execute();

            if (response.isSuccessful() && response.body() != null) {
                for (CategoryDto dto : response.body()) {
                    CategoryEntity remoteEntity = categoryRepository.mapDtoToEntity(dto);
                    CategoryEntity localEntity =
                            categoryRepository.getCategoryDao().getCategoryById(dto.getId());

                    if (localEntity == null) {
                        categoryRepository.getCategoryDao().insert(remoteEntity);
                    } else if (localEntity.isSynced()) {
                        categoryRepository.getCategoryDao().insert(remoteEntity);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error pulling categories", e);
        }
    }

    public boolean isSyncing() {
        return isSyncing;
    }
}
