package com.example.progettotreest;

import android.content.Context;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Model {
    private static Model theInstance = null;
    private ArrayList<Line> lines = null;
    private String sid = null;

    public static synchronized Model getInstance() {
        if (theInstance == null) {
            theInstance = new Model();
        }
        return theInstance;
    }

    private Model() {
        lines = new ArrayList<Line>();
        sid = "Cez4i87enqRWx32e";
    }

    public Line get(int index) {
        return lines.get(index);
    }

    public int getSize() {
        return lines.size();
    }

    public void getLines(Context context, LinesAdapter adapter){
        CommunicationController.getLines(context, this.sid,
                response -> {
                    Log.d(LogTags.VOLLEY, "Just received lines " + response.toString());
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
                error -> Log.d(LogTags.VOLLEY, "ERRORE " + error.toString()));

    }



}
