package com.hormaz.studentplanner;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.hormaz.studentplanner.data.local.db.AppDatabase;
import com.hormaz.studentplanner.data.local.prefs.PreferencesManager;
import com.hormaz.studentplanner.data.remote.SupabaseClient;
import com.hormaz.studentplanner.data.repository.AuthRepository;
import com.hormaz.studentplanner.data.repository.CategoryRepository;
import com.hormaz.studentplanner.data.repository.TaskRepository;
import com.hormaz.studentplanner.data.repository.SyncManager;

public class App extends Application {

    private static App instance;

    private AppDatabase database;
    private PreferencesManager preferencesManager;
    private SupabaseClient supabaseClient;
    private AuthRepository authRepository;
    private TaskRepository taskRepository;
    private CategoryRepository categoryRepository;
    private SyncManager syncManager;

    public static final String CHANNEL_REMINDERS = "reminders";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Initialize core components
        preferencesManager = new PreferencesManager(this);
        database = AppDatabase.getInstance(this);
        supabaseClient = new SupabaseClient(preferencesManager);

        // Initialize repositories
        authRepository = new AuthRepository(
                supabaseClient.getAuthApi(),
                preferencesManager
        );
        taskRepository = new TaskRepository(
                database.taskDao(),
                database.subTaskDao(),
                supabaseClient.getDbApi(),
                preferencesManager
        );
        categoryRepository = new CategoryRepository(
                database.categoryDao(),
                supabaseClient.getDbApi(),
                preferencesManager
        );
        syncManager = new SyncManager(taskRepository, categoryRepository, preferencesManager);

        applyTheme();
        createNotificationChannels();
    }

    private void applyTheme() {
        String mode = preferencesManager.getThemeMode();
        if ("dark".equals(mode)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if ("light".equals(mode)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    private void createNotificationChannels() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_REMINDERS,
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("Reminders for upcoming task deadlines");
        channel.enableVibration(true);

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public SupabaseClient getSupabaseClient() {
        return supabaseClient;
    }

    public AuthRepository getAuthRepository() {
        return authRepository;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public SyncManager getSyncManager() {
        return syncManager;
    }
}
