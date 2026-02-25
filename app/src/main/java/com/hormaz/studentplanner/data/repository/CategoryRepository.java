package com.hormaz.studentplanner.data.repository;

import android.os.Handler;
import android.os.Looper;

import com.hormaz.studentplanner.data.local.db.dao.CategoryDao;
import com.hormaz.studentplanner.data.local.db.entity.CategoryEntity;
import com.hormaz.studentplanner.data.local.prefs.PreferencesManager;
import com.hormaz.studentplanner.data.model.Category;
import com.hormaz.studentplanner.data.remote.api.SupabaseDbApi;
import com.hormaz.studentplanner.data.remote.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRepository {

    private final CategoryDao categoryDao;
    private final SupabaseDbApi dbApi;
    private final PreferencesManager prefs;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public interface CategoryCallback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    public CategoryRepository(CategoryDao categoryDao, SupabaseDbApi dbApi,
                               PreferencesManager prefs) {
        this.categoryDao = categoryDao;
        this.dbApi = dbApi;
        this.prefs = prefs;
        this.executor = Executors.newFixedThreadPool(2);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void getAllCategories(CategoryCallback<List<Category>> callback) {
        executor.execute(() -> {
            try {
                String userId = prefs.getUserId();
                List<CategoryEntity> entities = categoryDao.getAllCategories(userId);
                List<Category> categories = mapEntitiesToCategories(entities);
                mainHandler.post(() -> callback.onSuccess(categories));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void getCategoryById(String categoryId, CategoryCallback<Category> callback) {
        executor.execute(() -> {
            try {
                CategoryEntity entity = categoryDao.getCategoryById(categoryId);
                if (entity != null) {
                    mainHandler.post(() -> callback.onSuccess(mapEntityToCategory(entity)));
                } else {
                    mainHandler.post(() -> callback.onError("Category not found"));
                }
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void createCategory(Category category, CategoryCallback<Category> callback) {
        executor.execute(() -> {
            try {
                if (category.getId() == null || category.getId().isEmpty()) {
                    category.setId(UUID.randomUUID().toString());
                }
                category.setUserId(prefs.getUserId());

                CategoryEntity entity = mapCategoryToEntity(category);
                categoryDao.insert(entity);
                mainHandler.post(() -> callback.onSuccess(category));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void updateCategory(Category category, CategoryCallback<Category> callback) {
        executor.execute(() -> {
            try {
                CategoryEntity entity = mapCategoryToEntity(category);
                entity.setSynced(false);
                entity.setLocalUpdatedAt(System.currentTimeMillis());
                categoryDao.update(entity);
                mainHandler.post(() -> callback.onSuccess(category));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void deleteCategory(String categoryId, CategoryCallback<Void> callback) {
        executor.execute(() -> {
            try {
                categoryDao.softDelete(categoryId, System.currentTimeMillis());
                mainHandler.post(() -> callback.onSuccess(null));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    // ── Sync Support ──

    public CategoryDao getCategoryDao() { return categoryDao; }
    public SupabaseDbApi getDbApi() { return dbApi; }
    public ExecutorService getExecutor() { return executor; }

    // ── Mappers ──

    private Category mapEntityToCategory(CategoryEntity entity) {
        Category category = new Category();
        category.setId(entity.getId());
        category.setUserId(entity.getUserId());
        category.setName(entity.getName());
        category.setColor(entity.getColor());
        category.setIcon(entity.getIcon());
        return category;
    }

    private List<Category> mapEntitiesToCategories(List<CategoryEntity> entities) {
        List<Category> categories = new ArrayList<>();
        for (CategoryEntity entity : entities) {
            categories.add(mapEntityToCategory(entity));
        }
        return categories;
    }

    private CategoryEntity mapCategoryToEntity(Category category) {
        CategoryEntity entity = new CategoryEntity(
                category.getId(), category.getUserId(),
                category.getName(), category.getColor()
        );
        entity.setIcon(category.getIcon());
        entity.setSynced(false);
        entity.setLocalUpdatedAt(System.currentTimeMillis());
        return entity;
    }

    public CategoryDto mapEntityToDto(CategoryEntity entity) {
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setName(entity.getName());
        dto.setColor(entity.getColor());
        dto.setIcon(entity.getIcon());
        dto.setDeleted(entity.isDeleted());
        return dto;
    }

    public CategoryEntity mapDtoToEntity(CategoryDto dto) {
        CategoryEntity entity = new CategoryEntity(
                dto.getId(), dto.getUserId(), dto.getName(), dto.getColor()
        );
        entity.setIcon(dto.getIcon());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setDeleted(dto.isDeleted());
        entity.setSynced(true);
        entity.setLocalUpdatedAt(System.currentTimeMillis());
        return entity;
    }
}
