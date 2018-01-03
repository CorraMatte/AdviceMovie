package com.example.corra.myapplication;

import android.arch.persistence.room.Entity;

import android.arch.persistence.room.PrimaryKey;
import javax.jdo.annotations.Unique;

/**
 * Created by corra on 02/01/18.
 */

@Entity
public class User {
    @PrimaryKey
    public String id;

    public String name;

    @Unique
    public String email;
}
