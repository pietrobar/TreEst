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
        Model.getInstance().initWithFakeData();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_linesDirections);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinesDirectionAdapter adapter = new LinesDirectionAdapter(this);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.imageButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileSettingsActivity.class);
            startActivity(intent);
        });

    }


}