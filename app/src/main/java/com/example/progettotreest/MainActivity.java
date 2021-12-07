package com.example.progettotreest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {
    LinesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences(MyStrings.PREFS, 0);
        String sid = sharedPref.getString("sid", "");

        RecyclerView recyclerView = findViewById(R.id.recyclerView_lines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LinesAdapter(this);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.imageButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileSettingsActivity.class);
            startActivity(intent);
        });
        if(sid.equals("")){//first time
            CommunicationController.register(this, adapter);//pass the adapter to update it at the end of the async call
        }else {//second time
            //Already registered
            Model.getInstance().setSid(sid);
            //retrieve info from shared preferences => saved did, then launch BoardActivity
            Gson gson = new Gson();
            String json = sharedPref.getString("terminus", "");
            Terminus preferredTerminus = gson.fromJson(json, Terminus.class);

            Intent intent = new Intent(this, BoardActivity.class);
            intent.putExtra("selectedTerminus", preferredTerminus);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //if I'm coming back from the boardActivity I have to check if there are lines
        SharedPreferences sharedPref = getSharedPreferences(MyStrings.PREFS, 0);
        String sid = sharedPref.getString("sid", "");
        if (!sid.equals("") && Model.getInstance().getLinesSize()==0){//the sid is needed because I want to know if it's the first acces
            Model.getInstance().retrieveLines(this,adapter);
        }
    }
}