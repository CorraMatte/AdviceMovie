package com.example.corra.myapplication.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by corra on 02/01/18.
 */

@Dao
public interface AdviceDao {

    @Insert
    public void insertAdvice(Advice... advices);

    @Delete
    public int deleteAdvice(Advice... advices);

    @Query("SELECT * FROM advice")
    public Advice[] getAllAdvices();

    @Query("SELECT * FROM advice WHERE id == :pid")
    public Advice getAdviceByID(int pid);
}
