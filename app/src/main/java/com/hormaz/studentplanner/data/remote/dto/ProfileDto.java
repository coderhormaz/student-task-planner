package com.hormaz.studentplanner.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class ProfileDto {

    @SerializedName("id")
    private String id;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("institution")
    private String institution;

    @SerializedName("streak_count")
    private int streakCount;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public int getStreakCount() { return streakCount; }
    public void setStreakCount(int streakCount) { this.streakCount = streakCount; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
