package com.example.progettotreest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_lines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinesAdapter adapter = new LinesAdapter(this);
        recyclerView.setAdapter(adapter);
        Model.getInstance().retrieveLines(this,adapter);

        findViewById(R.id.imageButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileSettingsActivity.class);
            startActivity(intent);
        });

    }


}