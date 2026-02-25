package com.hormaz.studentplanner.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hormaz.studentplanner.data.local.db.entity.TaskEntity;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TaskEntity task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TaskEntity> tasks);

    @Update
    void update(TaskEntity task);

    @Delete
    void delete(TaskEntity task);

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND is_deleted = 0 ORDER BY position ASC, created_at DESC")
    List<TaskEntity> getAllTasks(String userId);

    @Query("SELECT * FROM tasks WHERE id = :taskId AND is_deleted = 0")
    TaskEntity getTaskById(String taskId);

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND is_deleted = 0 " +
            "AND date(due_date) = date(:date) ORDER BY priority DESC, position ASC")
    List<TaskEntity> getTasksByDate(String userId, String date);

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND is_deleted = 0 " +
            "AND date(due_date) BETWEEN date(:startDate) AND date(:endDate) " +
            "ORDER BY due_date ASC, priority DESC")
    List<TaskEntity> getTasksByDateRange(String userId, String startDate, String endDate);

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND is_deleted = 0 " +
            "AND date(due_date) = date('now', 'localtime') " +
            "ORDER BY priority DESC, position ASC")
    List<TaskEntity> getTodaysTasks(String userId);

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND is_deleted = 0 " +
            "AND is_focus = 1 AND status != 'completed' " +
            "ORDER BY priority DESC LIMIT 4")
    List<TaskEntity> getFocusTasks(String userId);

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND is_deleted = 0 " +
            "AND category_id = :categoryId ORDER BY priority DESC, position ASC")
    List<TaskEntity> getTasksByCategory(String userId, String categoryId);

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND is_deleted = 0 " +
            "AND status = :status ORDER BY due_date ASC")
    List<TaskEntity> getTasksByStatus(String userId, String status);

    @Query("SELECT COUNT(*) FROM tasks WHERE user_id = :userId AND is_deleted = 0 " +
            "AND status = 'completed'")
    int getCompletedCount(String userId);

    @Query("SELECT COUNT(*) FROM tasks WHERE user_id = :userId AND is_deleted = 0")
    int getTotalCount(String userId);

    @Query("SELECT COUNT(*) FROM tasks WHERE user_id = :userId AND is_deleted = 0 " +
            "AND status = 'completed' AND date(completed_at) = date(:date)")
    int getCompletedCountByDate(String userId, String date);

    @Query("SELECT COUNT(*) FROM tasks WHERE user_id = :userId AND is_deleted = 0 " +
            "AND date(due_date) = date(:date)")
    int getTaskCountByDate(String userId, String date);

    @Query("SELECT * FROM tasks WHERE is_synced = 0")
    List<TaskEntity> getUnsyncedTasks();

    @Query("UPDATE tasks SET is_synced = 1 WHERE id = :taskId")
    void markSynced(String taskId);

    @Query("UPDATE tasks SET status = 'completed', completed_at = :completedAt, " +
            "is_synced = 0, local_updated_at = :localUpdatedAt WHERE id = :taskId")
    void completeTask(String taskId, String completedAt, long localUpdatedAt);

    @Query("UPDATE tasks SET is_deleted = 1, is_synced = 0, " +
            "local_updated_at = :localUpdatedAt WHERE id = :taskId")
    void softDelete(String taskId, long localUpdatedAt);

    @Query("SELECT DISTINCT date(due_date) FROM tasks WHERE user_id = :userId " +
            "AND is_deleted = 0 AND due_date IS NOT NULL " +
            "AND date(due_date) BETWEEN date(:startDate) AND date(:endDate)")
    List<String> getTaskDatesInRange(String userId, String startDate, String endDate);

    @Query("SELECT COUNT(*) FROM tasks WHERE user_id = :userId AND is_deleted = 0 " +
            "AND status = 'completed' AND category_id = :categoryId")
    int getCompletedCountByCategory(String userId, String categoryId);
}
