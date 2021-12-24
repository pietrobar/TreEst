package com.example.progettotreest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class BoardActivity extends AppCompatActivity {

    private PostsAdapter adapter=null;
    private int did=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //get selected terminus from previous activity
        Line line = (Line) getIntent().getSerializableExtra("line");
        int did = getIntent().getIntExtra("did", -1);
        TextView selectedDirectionTV = findViewById(R.id.selectedDirection_tv);
        selectedDirectionTV.setText(line.getNameBasedOnDid(did));

        RecyclerView recyclerView = findViewById(R.id.recyclerView_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PostsAdapter adapter = new PostsAdapter(this);
        recyclerView.setAdapter(adapter);


        this.adapter= adapter;
        this.did=did;

        Model.getInstance().retrievePosts(this, did, adapter);



        //DEFINE BUTTONS LISTENERS
        findViewById(R.id.new_post_btn).setOnClickListener(v -> {
            Intent intent = new Intent(this, NewPostActivity.class);
            intent.putExtra("did", did);
            startActivity(intent);
        });

        findViewById(R.id.switch_line_btn).setOnClickListener(v -> {
            SharedPreferences sharedPreferences = this.getSharedPreferences(MyStrings.PREFS, 0);
            int currentDid = sharedPreferences.getInt("did",-1);
            //invert terminus
            int newDid = line.getTerminus1().getDid()==currentDid ? line.getTerminus2().getDid() : line.getTerminus1().getDid();
            selectedDirectionTV.setText(line.getNameBasedOnDid(newDid));
            //save new did

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("did",newDid);
            editor.commit();
            Model.getInstance().retrievePosts(this, newDid, adapter);
        });

        findViewById(R.id.details_btn).setOnClickListener(v ->{
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("did",did);
            startActivity(intent);
        });

        findViewById(R.id.imageButton).setOnClickListener(v->{
            Intent intent = new Intent(this, ProfileSettingsActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //I want to update the posts content when I publish a post
        if (this.adapter!=null && this.did!=-1)
            Model.getInstance().retrievePosts(this, this.did, this.adapter);

    }
}