package com.hormaz.studentplanner.data.local.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "sub_tasks",
        indices = {
                @Index(value = {"task_id"})
        },
        foreignKeys = @ForeignKey(
                entity = TaskEntity.class,
                parentColumns = "id",
                childColumns = "task_id",
                onDelete = ForeignKey.CASCADE
        ))
public class SubTaskEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "task_id")
    @NonNull
    private String taskId;

    @ColumnInfo(name = "title")
    @NonNull
    private String title;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    @ColumnInfo(name = "position")
    private int position;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    @ColumnInfo(name = "is_synced")
    private boolean isSynced;

    @ColumnInfo(name = "local_updated_at")
    private long localUpdatedAt;

    public SubTaskEntity(@NonNull String id, @NonNull String taskId, @NonNull String title) {
        this.id = id;
        this.taskId = taskId;
        this.title = title;
        this.isCompleted = false;
        this.position = 0;
        this.isSynced = false;
        this.localUpdatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    @NonNull
    public String getTaskId() { return taskId; }
    public void setTaskId(@NonNull String taskId) { this.taskId = taskId; }

    @NonNull
    public String getTitle() { return title; }
    public void setTitle(@NonNull String title) { this.title = title; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public boolean isSynced() { return isSynced; }
    public void setSynced(boolean synced) { isSynced = synced; }

    public long getLocalUpdatedAt() { return localUpdatedAt; }
    public void setLocalUpdatedAt(long localUpdatedAt) { this.localUpdatedAt = localUpdatedAt; }
}
