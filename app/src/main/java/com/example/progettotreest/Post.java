package com.example.progettotreest;

import java.util.Date;

public class Post {
    private int delay;
    private int status;
    private String comment;
    private boolean followingAuthor;
    private String datetime;
    private String authorName;
    private int pversion;
    private int author;


    public Post(int delay, int status, String comment, boolean followingAuthor, String datetime, String authorName, int pversion, int author) {
        this.delay = delay;
        this.status = status;
        this.comment = comment;
        this.followingAuthor = followingAuthor;
        this.datetime = datetime;
        this.authorName = authorName;
        this.pversion = pversion;
        this.author = author;
    }

    public int getDelay() {
        return delay;
    }

    public int getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public boolean isFollowingAuthor() {
        return followingAuthor;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getPversion() {
        return pversion;
    }

    public int getAuthor() {
        return author;
    }

    public void setFollowingAuthor(boolean b) {
        this.followingAuthor=b;
    }
}
