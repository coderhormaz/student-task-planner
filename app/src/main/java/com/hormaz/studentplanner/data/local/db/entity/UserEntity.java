package com.hormaz.studentplanner.data.local.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "display_name")
    private String displayName;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "avatar_url")
    private String avatarUrl;

    @ColumnInfo(name = "institution")
    private String institution;

    @ColumnInfo(name = "streak_count")
    private int streakCount;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    public UserEntity(@NonNull String id, String displayName, String email) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.streakCount = 0;
    }

    // Getters and Setters
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

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
