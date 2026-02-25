package com.hormaz.studentplanner.data.remote.api;

import com.hormaz.studentplanner.data.remote.dto.LoginRequest;
import com.hormaz.studentplanner.data.remote.dto.LoginResponse;
import com.hormaz.studentplanner.data.remote.dto.SignupRequest;
import com.hormaz.studentplanner.data.remote.dto.RefreshTokenRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupabaseAuthApi {

    @POST("auth/v1/signup")
    Call<LoginResponse> signup(@Body SignupRequest request);

    @POST("auth/v1/token")
    Call<LoginResponse> login(
            @Query("grant_type") String grantType,
            @Body LoginRequest request
    );

    @POST("auth/v1/token")
    Call<LoginResponse> refreshToken(
            @Query("grant_type") String grantType,
            @Body RefreshTokenRequest request
    );

    @POST("auth/v1/logout")
    Call<Void> logout();
}
