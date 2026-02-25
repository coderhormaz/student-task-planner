package com.hormaz.studentplanner.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREF_NAME = "student_planner_prefs";

    // Auth keys
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_AVATAR_URL = "avatar_url";

    // App state keys
    private static final String KEY_ONBOARDING_COMPLETE = "onboarding_complete";
    private static final String KEY_LAST_SYNC_TIME = "last_sync_time";

    // Settings keys
    private static final String KEY_THEME_MODE = "theme_mode"; // light, dark, system, amoled
    private static final String KEY_DYNAMIC_COLOR = "dynamic_color";
    private static final String KEY_REDUCE_MOTION = "reduce_motion";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";
    private static final String KEY_DAILY_SUMMARY = "daily_summary";
    private static final String KEY_STREAK_REMINDER = "streak_reminder";

    private final SharedPreferences prefs;

    public PreferencesManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ── Auth ──

    public void saveAuthTokens(String accessToken, String refreshToken) {
        prefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .apply();
    }

    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }

    public void saveUserInfo(String userId, String email, String name, String avatarUrl) {
        prefs.edit()
                .putString(KEY_USER_ID, userId)
                .putString(KEY_USER_EMAIL, email)
                .putString(KEY_USER_NAME, name)
                .putString(KEY_AVATAR_URL, avatarUrl)
                .apply();
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "Student");
    }

    public String getAvatarUrl() {
        return prefs.getString(KEY_AVATAR_URL, null);
    }

    public boolean isLoggedIn() {
        return getAccessToken() != null && getUserId() != null;
    }

    public void clearAuth() {
        prefs.edit()
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .remove(KEY_USER_ID)
                .remove(KEY_USER_EMAIL)
                .remove(KEY_USER_NAME)
                .remove(KEY_AVATAR_URL)
                .apply();
    }

    // ── App State ──

    public boolean isOnboardingComplete() {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETE, false);
    }

    public void setOnboardingComplete(boolean complete) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETE, complete).apply();
    }

    public long getLastSyncTime() {
        return prefs.getLong(KEY_LAST_SYNC_TIME, 0);
    }

    public void setLastSyncTime(long time) {
        prefs.edit().putLong(KEY_LAST_SYNC_TIME, time).apply();
    }

    // ── Settings ──

    public String getThemeMode() {
        return prefs.getString(KEY_THEME_MODE, "system");
    }

    public void setThemeMode(String mode) {
        prefs.edit().putString(KEY_THEME_MODE, mode).apply();
    }

    public boolean isDynamicColorEnabled() {
        return prefs.getBoolean(KEY_DYNAMIC_COLOR, true);
    }

    public void setDynamicColorEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_DYNAMIC_COLOR, enabled).apply();
    }

    public boolean isReduceMotionEnabled() {
        return prefs.getBoolean(KEY_REDUCE_MOTION, false);
    }

    public void setReduceMotionEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_REDUCE_MOTION, enabled).apply();
    }

    public boolean isNotificationEnabled() {
        return prefs.getBoolean(KEY_NOTIFICATION_ENABLED, true);
    }

    public void setNotificationEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply();
    }

    public boolean isDailySummaryEnabled() {
        return prefs.getBoolean(KEY_DAILY_SUMMARY, true);
    }

    public void setDailySummaryEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_DAILY_SUMMARY, enabled).apply();
    }

    public boolean isStreakReminderEnabled() {
        return prefs.getBoolean(KEY_STREAK_REMINDER, true);
    }

    public void setStreakReminderEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_STREAK_REMINDER, enabled).apply();
    }

    public int getStreakCount() {
        return prefs.getInt("streak_count", 0);
    }

    public void setStreakCount(int count) {
        prefs.edit().putInt("streak_count", count).apply();
    }
}
