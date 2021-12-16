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









    //todo: considerare come opzione quella di fare un setContext e settare l'application context dalla main activity
    public void retrieveLines(Context context, LinesAdapter adapter){
        CommunicationController.getLines(context, this.sid,
                response -> {
                    Log.d(MyStrings.VOLLEY, "Just received lines " + response.toString());
                    try {

                        JSONArray linesJson = response.getJSONArray("lines");
                        for(int i = 0; i < linesJson.length(); i++) {
                            JSONObject line = linesJson.getJSONObject(i);
                            JSONObject t1 = line.getJSONObject("terminus1");
                            JSONObject t2 = line.getJSONObject("terminus2");
                            lines.add(new Line(new Terminus(t1.getString("sname"), t1.getInt("did")),
                                                new Terminus(t2.getString("sname"),t2.getInt("did"))));

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Log.d(MyStrings.VOLLEY, "ERRORE " + error.toString()));

    }

    public void retrievePosts(Context context, int did, PostsAdapter adapter) {
        posts.clear();
        CommunicationController.getPosts(context, this.sid, did,
                response->{
                    Log.d(MyStrings.VOLLEY,"Just Received posts: " + response.toString());
                    JSONArray postsJson = null;
                    try {
                        postsJson = response.getJSONArray("posts");
                        for(int i = 0; i < postsJson.length(); i++) {
                            JSONObject post = postsJson.getJSONObject(i);

                            String datetime =  post.getString("datetime");
                            String subDate = datetime.substring(0,datetime.indexOf("."));
                            posts.add(new Post(
                                    post.has("delay")?post.getInt("delay"):-1,
                                    post.has("status")?post.getInt("status"):-1,
                                    post.has("comment")?post.getString("comment"):"No Comment",
                                    post.getBoolean("followingAuthor"),
                                    subDate,
                                    post.getString("authorName"),
                                    post.getInt("pversion"),
                                    post.getInt("author")));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Collections.sort(posts, (o1, o2) -> Boolean.compare(!o1.isFollowingAuthor(),!o2.isFollowingAuthor()));
                    adapter.notifyDataSetChanged();

                },
                error->{Log.d(MyStrings.VOLLEY,"Errore "+error);});
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
}
