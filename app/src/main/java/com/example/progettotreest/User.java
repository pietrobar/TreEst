package com.example.progettotreest;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private int uid;

    @ColumnInfo
    private String name;
    @ColumnInfo
    private String picture;
    @ColumnInfo
    private String pversion;

    public User(int uid, String name, String picture, String pversion) {
        this.uid = uid;
        this.name = name;
        this.picture = picture;
        this.pversion = pversion;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPversion() {
        return pversion;
    }

    public void setPversion(String pversion) {
        this.pversion = pversion;
    }


}
