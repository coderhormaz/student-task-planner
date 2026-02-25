package com.hormaz.studentplanner.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hormaz.studentplanner.data.local.db.entity.SubTaskEntity;

import java.util.List;

@Dao
public interface SubTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SubTaskEntity subTask);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SubTaskEntity> subTasks);

    @Update
    void update(SubTaskEntity subTask);

    @Delete
    void delete(SubTaskEntity subTask);

    @Query("SELECT * FROM sub_tasks WHERE task_id = :taskId ORDER BY position ASC")
    List<SubTaskEntity> getSubTasksForTask(String taskId);

    @Query("SELECT * FROM sub_tasks WHERE is_synced = 0")
    List<SubTaskEntity> getUnsyncedSubTasks();

    @Query("UPDATE sub_tasks SET is_synced = 1 WHERE id = :subTaskId")
    void markSynced(String subTaskId);

    @Query("UPDATE sub_tasks SET is_completed = :completed, is_synced = 0, " +
            "local_updated_at = :localUpdatedAt WHERE id = :subTaskId")
    void toggleComplete(String subTaskId, boolean completed, long localUpdatedAt);

    @Query("DELETE FROM sub_tasks WHERE task_id = :taskId")
    void deleteAllForTask(String taskId);

    @Query("SELECT COUNT(*) FROM sub_tasks WHERE task_id = :taskId AND is_completed = 1")
    int getCompletedSubTaskCount(String taskId);

    @Query("SELECT COUNT(*) FROM sub_tasks WHERE task_id = :taskId")
    int getTotalSubTaskCount(String taskId);
}
