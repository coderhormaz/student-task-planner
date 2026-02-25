package com.hormaz.studentplanner.data.model;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private String id;
    private String userId;
    private String categoryId;
    private String categoryName;
    private String categoryColor;
    private String title;
    private String description;
    private String dueDate;
    private String reminderAt;
    private int priority; // 1=low, 2=medium, 3=high
    private String status; // pending, in_progress, completed, cancelled
    private boolean isFocus;
    private String completedAt;
    private int position;
    private String createdAt;
    private String updatedAt;
    private List<SubTask> subTasks;

    public Task() {
        this.subTasks = new ArrayList<>();
        this.priority = 1;
        this.status = "pending";
    }

    public Task(String id, String userId, String title) {
        this();
        this.id = id;
        this.userId = userId;
        this.title = title;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCategoryColor() { return categoryColor; }
    public void setCategoryColor(String categoryColor) { this.categoryColor = categoryColor; }

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

    public List<SubTask> getSubTasks() { return subTasks; }
    public void setSubTasks(List<SubTask> subTasks) { this.subTasks = subTasks; }

    public boolean isCompleted() {
        return "completed".equals(status);
    }

    public String getPriorityLabel() {
        switch (priority) {
            case 3: return "High";
            case 2: return "Medium";
            default: return "Low";
        }
    }
}
