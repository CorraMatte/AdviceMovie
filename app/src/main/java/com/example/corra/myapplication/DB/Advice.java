package com.example.corra.myapplication.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.corra.myapplication.User;

/**
 * Created by corra on 02/01/18.
 */

@Entity(foreignKeys = {@ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "id_user_sender"),
        @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "id_user_recv")})
public class Advice {
    @PrimaryKey
    public int id;

    public String id_user_sender;
    public String id_user_recv;

    public String id_movie;

    public String time;
    public String overview;
}
