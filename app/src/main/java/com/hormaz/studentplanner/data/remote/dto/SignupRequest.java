package com.hormaz.studentplanner.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class SignupRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("data")
    private UserMetadata data;

    public SignupRequest(String email, String password, String displayName) {
        this.email = email;
        this.password = password;
        this.data = new UserMetadata(displayName);
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserMetadata getData() { return data; }

    public static class UserMetadata {
        @SerializedName("display_name")
        private String displayName;

        public UserMetadata(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() { return displayName; }
    }
}
