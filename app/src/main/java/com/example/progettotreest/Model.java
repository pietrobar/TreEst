package com.example.progettotreest;

import android.content.Context;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Model {
    private static Model theInstance = null;
    private ArrayList<Line> lines = null;
    private ArrayList<Post> posts = null;
    private String sid = null;
    private Database db;
    private List<User> users;
    private PostsAdapter postAdapter;
    private String uid;


    public static synchronized Model getInstance() {
        if (theInstance == null) {
            theInstance = new Model();
        }
        return theInstance;
    }

    private Model() {
        lines = new ArrayList<>();
        posts = new ArrayList<>();

    }




    public Post getPost(int index) {
        return posts.get(index);
    }

    public int getPostsSize(){ return posts.size();}

    public Line getLine(int index) {
        return lines.get(index);
    }

    public int getLinesSize() {
        return lines.size();
    }

    public void setSid(String sid) {
        this.sid=sid;
    }

    public String getSid() {
        return sid;
    }

    public void setDB(Database db) {
        this.db = db;
    }

    public Database getDB() {
        return this.db;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void refreshPostsFollowsLocally(Post post) {
        for (Post toUpdate: posts) {
            if(toUpdate.getAuthor()==post.getAuthor()){
                toUpdate.setFollowingAuthor(post.isFollowingAuthor());
            }
        }
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    public void clearPosts() {
        this.posts.clear();
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void sortPosts() {
        Collections.sort(posts, (o1, o2) -> Boolean.compare(!o1.isFollowingAuthor(),!o2.isFollowingAuthor()));

    }

    public PostsAdapter getPostAdapter() {
        return this.postAdapter;
    }

    public void setPostAdapter(PostsAdapter adapter){
        this.postAdapter = adapter;
    }

    public void setUid(String uid) {
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }
}
