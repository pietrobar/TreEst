package com.example.progettotreest;

import static com.example.progettotreest.MyStrings.VOLLEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    LinesAdapter adapter;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), Database.class, "db").build();
        Model.getInstance().setDB(db);

        SharedPreferences sharedPref = getSharedPreferences(MyStrings.PREFS, 0);
        String sid = sharedPref.getString("sid", "");

        RecyclerView recyclerView = findViewById(R.id.recyclerView_lines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LinesAdapter(this);


        findViewById(R.id.imageButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileSettingsActivity.class);
            startActivity(intent);
        });
        if(sid.equals("")){//first time
            CommunicationController.register(this, response -> handleResponse(response, sharedPref),error -> Log.d(VOLLEY, error.toString()));
        }else if(!sid.equals("") && sharedPref.getInt("did", -1)==-1){//second access BUT did was not set
            Model.getInstance().setSid(sid);
            CommunicationController.retrieveLines(this, adapter);
        }else {//second time
            //Already registered
            Model.getInstance().setSid(sid);
            //retrieve info from shared preferences => line and did, then launch BoardActivity
            Gson gson = new Gson();
            String json = sharedPref.getString("line", "");
            Line line = gson.fromJson(json, Line.class);
            int did = sharedPref.getInt("did", -1);
            Intent intent = new Intent(this, BoardActivity.class);
            intent.putExtra("line", line);
            intent.putExtra("did", did);
            startActivity(intent);
        }

        recyclerView.setAdapter(adapter);

    }

    private void handleResponse(JSONObject response, SharedPreferences sharedPref) {
        {
            try {
                Log.d(VOLLEY, "Received: " + response.toString());
                //save the sid to shared preference
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("sid", response.getString("sid"));
                editor.commit();
                //set sid to model and retrieve the lines
                Model.getInstance().setSid(response.getString("sid"));
                CommunicationController.retrieveLines(this, adapter);
                CommunicationController.getProfile(this,Model.getInstance().getSid(), res->{
                    Log.d(VOLLEY, "Received: " + res.toString());
                    //I want to save into shared preferences the UID
                    try {
                        editor.putString("uid", res.getString("uid"));
                        Model.getInstance().setUid(res.getString("uid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.commit();
                },err->Log.d(MyStrings.VOLLEY, err.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }}
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if I'm coming back from the boardActivity I have to check if there are lines
        SharedPreferences sharedPref = getSharedPreferences(MyStrings.PREFS, 0);
        String sid = sharedPref.getString("sid", "");
        if (!sid.equals("") && Model.getInstance().getLinesSize()==0 && sharedPref.getInt("did", -1)!=-1){//the sid is needed because I want to know if it's the first acces
            CommunicationController.retrieveLines(this,adapter);
        }
    }


}