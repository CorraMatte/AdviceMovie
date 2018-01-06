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

    User(String id, String name){
        this.id = id;
        this.name = name;
    }

    User(String id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
