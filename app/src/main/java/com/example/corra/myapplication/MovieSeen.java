package com.example.corra.myapplication;

/**
 * Created by corra on 02/01/18.
 */


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "id_user")})
public class MovieSeen {
    @PrimaryKey
    public int id;

    public String user_id;
    public String movie_id;

    public boolean accepted;
}
