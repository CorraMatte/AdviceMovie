package com.example.corra.myapplication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by corra on 02/01/18.
 */

@Dao
public interface UserDao {

    @Insert
    public long[] insertUsers(User... users);

    @Query("SELECT * FROM user WHERE id == :pid")
    public User getUserByID(String pid);
}
