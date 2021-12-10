package com.example.progettotreest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //get selected terminus from previous activity
        Line line = (Line) getIntent().getSerializableExtra("line");
        int did = getIntent().getIntExtra("did", -1);
        TextView selectedDirectionTV = findViewById(R.id.selectedDirection_tv);
        //todo: settare il nome della linea e la direzione
        selectedDirectionTV.setText(line.getNameBasedOnDid(did));

        RecyclerView recyclerView = findViewById(R.id.recyclerView_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PostsAdapter adapter = new PostsAdapter(this);
        recyclerView.setAdapter(adapter);

        Model.getInstance().retrievePosts(this, did, adapter);

        findViewById(R.id.new_post_btn).setOnClickListener(v -> {
            Intent intent = new Intent(this, NewPostActivity.class);
            intent.putExtra("did", did);
            startActivity(intent);
        });

    }
}