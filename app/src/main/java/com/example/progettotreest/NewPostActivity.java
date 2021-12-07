package com.example.progettotreest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        //get did from previous activity
        int did = getIntent().getIntExtra("did",0);

        //get all the input text field
        EditText delayIT = findViewById(R.id.delay_input_text);
        EditText statusIT = findViewById(R.id.status_input_text);
        EditText commentIT = findViewById(R.id.comment_input_text);

        findViewById(R.id.publish_btn).setOnClickListener(v -> {
            //todo: controlla che almeno uno degli edit text sia pieno


            CommunicationController.addPost(this, Model.getInstance().getSid(),did,
                    Integer.parseInt(delayIT.getText().toString()),
                    Integer.parseInt(statusIT.getText().toString()),
                    commentIT.getText().toString(),
                    response -> {
                        Log.d(MyStrings.VOLLEY, "Just added a post");
                    },
                    error -> {
                        Log.d(MyStrings.VOLLEY, error.toString());
                    });

            super.onBackPressed();
        });

    }
}