package com.hormaz.studentplanner.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class TaskDto {

    @SerializedName("id")
    private String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("due_date")
    private String dueDate;

    @SerializedName("reminder_at")
    private String reminderAt;

    @SerializedName("priority")
    private int priority;

    @SerializedName("status")
    private String status;

    @SerializedName("is_focus")
    private boolean isFocus;

    @SerializedName("completed_at")
    private String completedAt;

    @SerializedName("position")
    private int position;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("is_deleted")
    private boolean isDeleted;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getReminderAt() { return reminderAt; }
    public void setReminderAt(String reminderAt) { this.reminderAt = reminderAt; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

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
}
