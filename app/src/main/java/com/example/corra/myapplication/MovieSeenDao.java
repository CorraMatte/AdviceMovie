package com.example.corra.myapplication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by corra on 02/01/18.
 */

@Dao
public interface MovieSeenDao {
    @Insert
    public void insertMovieSeen(MovieSeen... movies);

    @Query("SELECT * FROM movieseen")
    public MovieSeen[] getAllMovieSeen();
}
