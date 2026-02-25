package com.hormaz.studentplanner.data.local.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories",
        indices = {
                @Index(value = {"user_id"}),
                @Index(value = {"user_id", "name"}, unique = true)
        },
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE
        ))
public class CategoryEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "user_id")
    @NonNull
    private String userId;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "color")
    @NonNull
    private String color;

    @ColumnInfo(name = "icon")
    private String icon;

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

    public CategoryEntity(@NonNull String id, @NonNull String userId, @NonNull String name, @NonNull String color) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.color = color;
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

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    @NonNull
    public String getColor() { return color; }
    public void setColor(@NonNull String color) { this.color = color; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

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
