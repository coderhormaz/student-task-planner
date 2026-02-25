package com.hormaz.studentplanner.data.model;

public class Category {

    private String id;
    private String userId;
    private String name;
    private String color;
    private String icon;
    private int taskCount;

    public Category() {}

    public Category(String id, String userId, String name, String color) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.color = color;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public int getTaskCount() { return taskCount; }
    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }
}
