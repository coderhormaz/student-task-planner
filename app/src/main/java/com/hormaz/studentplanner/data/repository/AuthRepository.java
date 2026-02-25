package com.hormaz.studentplanner.data.repository;

import com.hormaz.studentplanner.data.local.prefs.PreferencesManager;
import com.hormaz.studentplanner.data.remote.api.SupabaseAuthApi;
import com.hormaz.studentplanner.data.remote.dto.LoginRequest;
import com.hormaz.studentplanner.data.remote.dto.LoginResponse;
import com.hormaz.studentplanner.data.remote.dto.RefreshTokenRequest;
import com.hormaz.studentplanner.data.remote.dto.SignupRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final SupabaseAuthApi authApi;
    private final PreferencesManager prefs;

    public interface AuthCallback {
        void onSuccess(LoginResponse response);
        void onError(String message);
    }

    public AuthRepository(SupabaseAuthApi authApi, PreferencesManager prefs) {
        this.authApi = authApi;
        this.prefs = prefs;
    }

    public void login(String email, String password, AuthCallback callback) {
        LoginRequest request = new LoginRequest(email, password);
        authApi.login("password", request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse body = response.body();
                    saveSession(body);
                    callback.onSuccess(body);
                } else {
                    callback.onError("Invalid email or password");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void signup(String email, String password, String name, AuthCallback callback) {
        SignupRequest request = new SignupRequest(email, password, name);
        authApi.signup(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse body = response.body();
                    saveSession(body);
                    callback.onSuccess(body);
                } else {
                    callback.onError("Signup failed. Email may already be in use.");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void refreshToken(AuthCallback callback) {
        String refreshToken = prefs.getRefreshToken();
        if (refreshToken == null) {
            callback.onError("No refresh token available");
            return;
        }

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        authApi.refreshToken("refresh_token", request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse body = response.body();
                    saveSession(body);
                    callback.onSuccess(body);
                } else {
                    callback.onError("Token refresh failed");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void logout() {
        try {
            authApi.logout().execute();
        } catch (Exception ignored) {}
        prefs.clearAuth();
    }

    public boolean isLoggedIn() {
        return prefs.isLoggedIn();
    }

    public String getUserId() {
        return prefs.getUserId();
    }

    public String getUserName() {
        return prefs.getUserName();
    }

    private void saveSession(LoginResponse response) {
        prefs.saveAuthTokens(response.getAccessToken(), response.getRefreshToken());
        if (response.getUser() != null) {
            LoginResponse.UserInfo user = response.getUser();
            String name = "";
            String avatar = null;
            if (user.getUserMetadata() != null) {
                name = user.getUserMetadata().getDisplayName();
                avatar = user.getUserMetadata().getAvatarUrl();
            }
            prefs.saveUserInfo(user.getId(), user.getEmail(), name, avatar);
        }
    }
}
