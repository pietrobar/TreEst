package com.example.progettotreest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Communication {
    private static final String URL = "https://ewserver.di.unimi.it/mobicomp/treest/";

    public static boolean register(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, URL+"register.php",
                response -> Log.d("TESTTEST", "Received: " + response),
                error -> Log.d("TESTTEST", "Ops: " + error.toString()));
        queue.add(request);
        return false;
    }


}
