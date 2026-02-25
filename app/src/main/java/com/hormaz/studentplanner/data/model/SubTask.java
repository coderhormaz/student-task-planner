package com.hormaz.studentplanner.data.model;

public class SubTask {

    private String id;
    private String taskId;
    private String title;
    private boolean isCompleted;
    private int position;

    public SubTask() {}

    public SubTask(String id, String taskId, String title) {
        this.id = id;
        this.taskId = taskId;
        this.title = title;
        this.isCompleted = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}
