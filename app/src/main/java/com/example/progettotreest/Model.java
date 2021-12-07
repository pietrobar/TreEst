package com.example.progettotreest;

import android.content.Context;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Model {
    private static Model theInstance = null;
    private ArrayList<Line> lines = null;
    private ArrayList<Post> posts = null;
    private String sid = null;



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
                    Log.d(MyStrings.PROVA,"Just Received posts: " + response.toString());
                    JSONArray postsJson = null;
                    try {
                        postsJson = response.getJSONArray("posts");
                        for(int i = 0; i < postsJson.length(); i++) {
                            JSONObject post = postsJson.getJSONObject(i);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


                            posts.add(new Post(
                                    post.has("delay")?post.getInt("delay"):-1,
                                    post.has("status")?post.getInt("status"):-1,
                                    post.has("comment")?post.getString("comment"):"No Comment",
                                    post.getBoolean("followingAuthor"),
                                    //TODO:LocalDateTime.parse(post.getString("datetime"), formatter),
                                    LocalDateTime.now(),
                                    post.getString("authorName"),
                                    post.getInt("pversion"),
                                    post.getInt("author")));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
}
