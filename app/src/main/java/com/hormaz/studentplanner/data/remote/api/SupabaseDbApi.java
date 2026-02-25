package com.hormaz.studentplanner.data.remote.api;

import com.hormaz.studentplanner.data.remote.dto.CategoryDto;
import com.hormaz.studentplanner.data.remote.dto.ProfileDto;
import com.hormaz.studentplanner.data.remote.dto.SubTaskDto;
import com.hormaz.studentplanner.data.remote.dto.TaskDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupabaseDbApi {

    // ── Profiles ──

    @GET("rest/v1/profiles")
    Call<List<ProfileDto>> getProfile(
            @Query("id") String idFilter,        // eq.{userId}
            @Query("select") String select
    );

    @POST("rest/v1/profiles")
    Call<Void> createProfile(
            @Body ProfileDto profile,
            @Header("Prefer") String prefer       // "return=minimal"
    );

    @PATCH("rest/v1/profiles")
    Call<Void> updateProfile(
            @Query("id") String idFilter,
            @Body ProfileDto profile
    );

    // ── Tasks ──

    @GET("rest/v1/tasks")
    Call<List<TaskDto>> getTasks(
            @Query("user_id") String userIdFilter,  // eq.{userId}
            @Query("is_deleted") String isDeleted,   // eq.false
            @Query("select") String select,
            @Query("order") String order,
            @Query("updated_at") String updatedAtFilter  // gt.{timestamp} for sync
    );

    @POST("rest/v1/tasks")
    Call<Void> createTask(
            @Body TaskDto task,
            @Header("Prefer") String prefer
    );

    @POST("rest/v1/tasks")
    Call<Void> upsertTask(
            @Body TaskDto task,
            @Header("Prefer") String prefer  // "resolution=merge-duplicates,return=minimal"
    );

    @PATCH("rest/v1/tasks")
    Call<Void> updateTask(
            @Query("id") String idFilter,
            @Body TaskDto task
    );

    @DELETE("rest/v1/tasks")
    Call<Void> deleteTask(
            @Query("id") String idFilter
    );

    // ── Sub Tasks ──

    @GET("rest/v1/sub_tasks")
    Call<List<SubTaskDto>> getSubTasks(
            @Query("task_id") String taskIdFilter,
            @Query("select") String select
    );

    @POST("rest/v1/sub_tasks")
    Call<Void> upsertSubTask(
            @Body SubTaskDto subTask,
            @Header("Prefer") String prefer
    );

    @PATCH("rest/v1/sub_tasks")
    Call<Void> updateSubTask(
            @Query("id") String idFilter,
            @Body SubTaskDto subTask
    );

    @DELETE("rest/v1/sub_tasks")
    Call<Void> deleteSubTask(
            @Query("id") String idFilter
    );

    // ── Categories ──

    @GET("rest/v1/categories")
    Call<List<CategoryDto>> getCategories(
            @Query("user_id") String userIdFilter,
            @Query("is_deleted") String isDeleted,
            @Query("select") String select,
            @Query("order") String order
    );

    @POST("rest/v1/categories")
    Call<Void> upsertCategory(
            @Body CategoryDto category,
            @Header("Prefer") String prefer
    );

    @PATCH("rest/v1/categories")
    Call<Void> updateCategory(
            @Query("id") String idFilter,
            @Body CategoryDto category
    );

    @DELETE("rest/v1/categories")
    Call<Void> deleteCategory(
            @Query("id") String idFilter
    );
}
