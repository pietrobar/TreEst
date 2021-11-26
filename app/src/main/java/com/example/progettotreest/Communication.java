package com.example.progettotreest;

import static com.example.progettotreest.LogTags.VOLLEY;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Communication {
    private static final String URL = "https://ewserver.di.unimi.it/mobicomp/treest/";
    private static final String GET_PROFILE = "getProfile.php";
    private static final String GET_LINES = "getLines.php";

    private String jsonSID = "{\"sid\":\"Cez4i87enqRWx32e\"}";


    public static boolean register(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, URL+"register.php",
                response -> Log.d("TESTTEST", "Received: " + response),
                error -> Log.d("TESTTEST", "Ops: " + error.toString()));
        queue.add(request);
        return false;
    }
    public static void getProfile(Context context, String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        sendPostRequest(GET_PROFILE, context,sid,responseListener,errorListener);
        // response -> {"uid":"181","name":"unnamed","picture":null,"pversion":"0"}
    }



    public static void getLines(Context context, String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        sendPostRequest(GET_LINES, context,sid,responseListener,errorListener);
        //response -> {"lines":[{"terminus1":{"sname":"Milano Celoria","did":1},"terminus2":{"sname":"Milano Rogoredo","did":2}},{"terminus1":{"sname":"Milano Lambrate","did":3},"terminus2":{"sname":"Sesto San Giovanni","did":4}}]}

    }

    private static void sendPostRequest(String urlEnd, Context context, String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                URL + urlEnd,
                jsonBody,
                responseListener,
                errorListener
        );
        Log.d(VOLLEY, "Sending request");
        queue.add(request);
    }


}
