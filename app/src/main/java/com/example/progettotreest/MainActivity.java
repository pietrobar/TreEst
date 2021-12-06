package com.example.progettotreest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences(MyStrings.PREFS, 0);
        String sid = sharedPref.getString("sid", "");
        if(sid.equals("")){
            CommunicationController.register(this);
            launchChooseLineActivity();
        }else {
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

    private void launchChooseLineActivity() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView_lines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinesAdapter adapter = new LinesAdapter(this);
        recyclerView.setAdapter(adapter);
        Model.getInstance().setLinesAdapter(adapter);//todo: non mi piace -> da rivedere

        findViewById(R.id.imageButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileSettingsActivity.class);
            startActivity(intent);
        });
    }


}