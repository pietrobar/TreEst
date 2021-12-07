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

import org.json.JSONException;
import org.json.JSONObject;

public class CommunicationController {
    private static final String URL = "https://ewserver.di.unimi.it/mobicomp/treest/";
    private static final String GET_PROFILE = "getProfile.php";
    private static final String GET_LINES = "getLines.php";
    private static final String GET_POSTS = "getPosts.php";
    private static final String REGISTER = "register.php";

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
                Model.getInstance().retrieveLines(context, adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }},error -> Log.d(VOLLEY, error.toString()));
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
