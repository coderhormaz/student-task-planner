package com.hormaz.studentplanner.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hormaz.studentplanner.data.local.db.entity.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryEntity category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryEntity> categories);

    @Update
    void update(CategoryEntity category);

    @Delete
    void delete(CategoryEntity category);

    @Query("SELECT * FROM categories WHERE user_id = :userId AND is_deleted = 0 ORDER BY name ASC")
    List<CategoryEntity> getAllCategories(String userId);

    @Query("SELECT * FROM categories WHERE id = :categoryId AND is_deleted = 0")
    CategoryEntity getCategoryById(String categoryId);

    @Query("SELECT * FROM categories WHERE is_synced = 0")
    List<CategoryEntity> getUnsyncedCategories();

    @Query("UPDATE categories SET is_synced = 1 WHERE id = :categoryId")
    void markSynced(String categoryId);

    @Query("UPDATE categories SET is_deleted = 1, is_synced = 0, " +
            "local_updated_at = :localUpdatedAt WHERE id = :categoryId")
    void softDelete(String categoryId, long localUpdatedAt);
}
