package com.hormaz.studentplanner.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("expires_in")
    private int expiresIn;

    @SerializedName("user")
    private UserInfo user;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getTokenType() { return tokenType; }
    public int getExpiresIn() { return expiresIn; }
    public UserInfo getUser() { return user; }

    public static class UserInfo {
        @SerializedName("id")
        private String id;

        @SerializedName("email")
        private String email;

        @SerializedName("user_metadata")
        private UserMetadata userMetadata;

        public String getId() { return id; }
        public String getEmail() { return email; }
        public UserMetadata getUserMetadata() { return userMetadata; }
    }

    public static class UserMetadata {
        @SerializedName("display_name")
        private String displayName;

        @SerializedName("avatar_url")
        private String avatarUrl;

        public String getDisplayName() { return displayName; }
        public String getAvatarUrl() { return avatarUrl; }
    }
}
