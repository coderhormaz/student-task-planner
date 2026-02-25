package com.hormaz.studentplanner.data.model;

public class User {

    private String id;
    private String email;
    private String displayName;
    private String avatarUrl;
    private String institution;
    private int streakCount;

    public User() {}

    public User(String id, String email, String displayName) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public int getStreakCount() { return streakCount; }
    public void setStreakCount(int streakCount) { this.streakCount = streakCount; }

    public String getFirstName() {
        if (displayName == null || displayName.isEmpty()) return "Student";
        String[] parts = displayName.split(" ");
        return parts[0];
    }
}
