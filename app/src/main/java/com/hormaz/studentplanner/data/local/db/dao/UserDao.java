package com.hormaz.studentplanner.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hormaz.studentplanner.data.local.db.entity.UserEntity;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Query("SELECT * FROM users WHERE id = :userId")
    UserEntity getUserById(String userId);

    @Query("DELETE FROM users WHERE id = :userId")
    void deleteUser(String userId);
}
