package com.example.progettotreest;

import static com.example.progettotreest.MyStrings.VOLLEY;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class CommunicationController {
    private static final String URL = "https://ewserver.di.unimi.it/mobicomp/treest/";
    private static final String GET_PROFILE = "getProfile.php";
    private static final String GET_LINES = "getLines.php";
    private static final String GET_POSTS = "getPosts.php";
    private static final String REGISTER = "register.php";
    private static final String FOLLOW = "follow.php";
    private static final String UNFOLLOW = "unfollow.php";
    private static final String ADD_POST = "addPost.php";
    private static final String GET_USER_PICTURE = "getUserPicture.php";
    private static final String GET_STATIONS = "getStations.php";
    private static final String SET_PROFILE = "setProfile.php";


    public static void register(Context context, LinesAdapter adapter){
        sendPostRequest(REGISTER,context,new JSONObject(), response -> {
            try {
                Log.d(VOLLEY, "Received: " + response.toString());
                SharedPreferences sharedPref = context.getSharedPreferences(MyStrings.PREFS, 0);
                //save the sid to shared preference
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("sid", response.getString("sid"));
                editor.commit();
                //set sid to model and retrieve the lines
                Model.getInstance().setSid(response.getString("sid"));
                CommunicationController.retrieveLines(context, adapter);
                CommunicationController.getProfile(context,Model.getInstance().getSid(), res->{
                    Log.d(VOLLEY, "Received: " + res.toString());
                    //I want to save into shared preferences the UID
                    try {
                        editor.putString("uid", res.getString("uid"));
                        Model.getInstance().setUid(res.getString("uid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.commit();
                },err->{Log.d(MyStrings.VOLLEY, err.toString());});
            } catch (JSONException e) {
                e.printStackTrace();
            }},error -> Log.d(VOLLEY, error.toString()));
    }

    public static void retrieveLines(Context context, LinesAdapter adapter){
        CommunicationController.getLines(context, Model.getInstance().getSid(),
                response -> {
                    Log.d(MyStrings.VOLLEY, "Just received lines " + response.toString());
                    try {

                        JSONArray linesJson = response.getJSONArray("lines");
                        for(int i = 0; i < linesJson.length(); i++) {
                            JSONObject line = linesJson.getJSONObject(i);
                            JSONObject t1 = line.getJSONObject("terminus1");
                            JSONObject t2 = line.getJSONObject("terminus2");
                            Model.getInstance().addLine(new Line(new Terminus(t1.getString("sname"), t1.getInt("did")),
                                    new Terminus(t2.getString("sname"),t2.getInt("did"))));

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Log.d(MyStrings.VOLLEY, "ERRORE " + error.toString()));

    }

    public static void retrievePosts(Context context, int did, PostsAdapter adapter) {
        Model.getInstance().clearPosts();
        CommunicationController.getPosts(context, Model.getInstance().getSid(), did,
                response->{
                    Log.d(MyStrings.VOLLEY,"Just Received posts: " + response.toString());
                    JSONArray postsJson = null;
                    try {
                        postsJson = response.getJSONArray("posts");
                        for(int i = 0; i < postsJson.length(); i++) {
                            JSONObject post = postsJson.getJSONObject(i);

                            String datetime =  post.getString("datetime");
                            String subDate = datetime.substring(0,datetime.indexOf("."));
                            Model.getInstance().addPost(new Post(
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
                    Model.getInstance().sortPosts();
                    adapter.notifyDataSetChanged();

                },
                error->{Log.d(MyStrings.VOLLEY,"Errore "+error);});
    }

    public static void getProfile(Context context, String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest(GET_PROFILE, context,jsonBody,responseListener,errorListener);
        // response -> {"uid":"181","name":"unnamed","picture":null,"pversion":"0"}
    }


    public static void getPosts(Context context, String sid, int did, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", sid);
            jsonBody.put("did",did);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest(GET_POSTS, context,jsonBody,responseListener,errorListener);

    }



    public static void getLines(Context context, String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest(GET_LINES, context,jsonBody,responseListener,errorListener);
    }

    public static void follow(Context context, String sid, int uid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", sid);
            jsonBody.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest(FOLLOW, context,jsonBody,responseListener,errorListener);
    }

    public static void unfollow(Context context, String sid, int uid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", sid);
            jsonBody.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest(UNFOLLOW, context,jsonBody,responseListener,errorListener);
    }

    public static void addPost(Context context, String sid, int did, int delay, int status, String comment, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", sid);
            jsonBody.put("did", did);
            if (delay!=-1)
                jsonBody.put("delay", delay);
            if (status!=-1)
                jsonBody.put("status", status);
            if (comment.length()!= 0)
                jsonBody.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest(ADD_POST, context,jsonBody,responseListener,errorListener);

    }

    public static void getUserPicture(Context context, String sid, int uid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", sid);
            jsonBody.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest(GET_USER_PICTURE, context,jsonBody,responseListener,errorListener);
    }

    public static void setProfile(Context context, String sid, String picture,String name, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try {

            jsonBody.put("sid", sid);
            if (name!=null)
                jsonBody.put("name", name);
            if (picture!=null)
                jsonBody.put("picture", picture);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest(SET_PROFILE, context,jsonBody,responseListener,errorListener);
    }

    public static void getStations(Context context, String sid, int did, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", sid);
            jsonBody.put("did", did);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest(GET_STATIONS, context,jsonBody,responseListener,errorListener);
    }


    private static void sendPostRequest(String urlEnd, Context context, JSONObject jsonBody, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                URL + urlEnd,
                jsonBody,
                responseListener,
                errorListener
        );
        Log.d(VOLLEY, "Sending request to " + urlEnd);
        queue.add(request);
    }



}
