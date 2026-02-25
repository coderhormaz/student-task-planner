package com.hormaz.studentplanner.data.local.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks",
        indices = {
                @Index(value = {"user_id"}),
                @Index(value = {"user_id", "due_date"}),
                @Index(value = {"user_id", "status"}),
                @Index(value = {"category_id"})
        },
        foreignKeys = {
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = CategoryEntity.class,
                        parentColumns = "id",
                        childColumns = "category_id",
                        onDelete = ForeignKey.SET_NULL
                )
        })
public class TaskEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "user_id")
    @NonNull
    private String userId;

    @ColumnInfo(name = "category_id")
    private String categoryId;

    @ColumnInfo(name = "title")
    @NonNull
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "due_date")
    private String dueDate;

    @ColumnInfo(name = "reminder_at")
    private String reminderAt;

    @ColumnInfo(name = "priority")
    private int priority; // 1=low, 2=medium, 3=high

    @ColumnInfo(name = "status")
    @NonNull
    private String status; // pending, in_progress, completed, cancelled

    @ColumnInfo(name = "is_focus")
    private boolean isFocus;

    @ColumnInfo(name = "completed_at")
    private String completedAt;

    @ColumnInfo(name = "position")
    private int position;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    @ColumnInfo(name = "is_deleted")
    private boolean isDeleted;

    @ColumnInfo(name = "is_synced")
    private boolean isSynced;

    @ColumnInfo(name = "local_updated_at")
    private long localUpdatedAt;

    public TaskEntity(@NonNull String id, @NonNull String userId, @NonNull String title) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.priority = 1;
        this.status = "pending";
        this.isFocus = false;
        this.position = 0;
        this.isDeleted = false;
        this.isSynced = false;
        this.localUpdatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    @NonNull
    public String getUserId() { return userId; }
    public void setUserId(@NonNull String userId) { this.userId = userId; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    @NonNull
    public String getTitle() { return title; }
    public void setTitle(@NonNull String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getReminderAt() { return reminderAt; }
    public void setReminderAt(String reminderAt) { this.reminderAt = reminderAt; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    @NonNull
    public String getStatus() { return status; }
    public void setStatus(@NonNull String status) { this.status = status; }

    public boolean isFocus() { return isFocus; }
    public void setFocus(boolean focus) { isFocus = focus; }

    public String getCompletedAt() { return completedAt; }
    public void setCompletedAt(String completedAt) { this.completedAt = completedAt; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    public boolean isSynced() { return isSynced; }
    public void setSynced(boolean synced) { isSynced = synced; }

    public long getLocalUpdatedAt() { return localUpdatedAt; }
    public void setLocalUpdatedAt(long localUpdatedAt) { this.localUpdatedAt = localUpdatedAt; }
}
