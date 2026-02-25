package com.hormaz.studentplanner.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hormaz.studentplanner.data.local.db.dao.CategoryDao;
import com.hormaz.studentplanner.data.local.db.dao.SubTaskDao;
import com.hormaz.studentplanner.data.local.db.dao.TaskDao;
import com.hormaz.studentplanner.data.local.db.dao.UserDao;
import com.hormaz.studentplanner.data.local.db.entity.CategoryEntity;
import com.hormaz.studentplanner.data.local.db.entity.SubTaskEntity;
import com.hormaz.studentplanner.data.local.db.entity.TaskEntity;
import com.hormaz.studentplanner.data.local.db.entity.UserEntity;

@Database(
        entities = {
                UserEntity.class,
                TaskEntity.class,
                SubTaskEntity.class,
                CategoryEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract TaskDao taskDao();
    public abstract SubTaskDao subTaskDao();
    public abstract CategoryDao categoryDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "student_planner_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
