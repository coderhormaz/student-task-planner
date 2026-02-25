package com.hormaz.studentplanner.util;

public class Constants {

    // Supabase
    public static final String SUPABASE_FILTER_EQ = "eq.";
    public static final String SUPABASE_FILTER_GT = "gt.";
    public static final String SUPABASE_SELECT_ALL = "*";
    public static final String SUPABASE_PREFER_MINIMAL = "return=minimal";
    public static final String SUPABASE_PREFER_UPSERT = "resolution=merge-duplicates,return=minimal";

    // Task Status
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_IN_PROGRESS = "in_progress";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_CANCELLED = "cancelled";

    // Priority
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_HIGH = 3;

    // Intent Extras
    public static final String EXTRA_TASK_ID = "extra_task_id";
    public static final String EXTRA_SELECTED_DATE = "extra_selected_date";
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";

    // Request Codes
    public static final int REQUEST_ADD_TASK = 100;
    public static final int REQUEST_EDIT_TASK = 101;
    public static final int REQUEST_PICK_IMAGE = 102;

    // Notification
    public static final int NOTIFICATION_ID_REMINDER = 1001;

    // Animation Durations
    public static final int ANIM_DURATION_SHORT = 200;
    public static final int ANIM_DURATION_MEDIUM = 400;
    public static final int ANIM_DURATION_LONG = 600;

    private Constants() {
        // Prevent instantiation
    }
}
