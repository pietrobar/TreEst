package com.example.progettotreest;

import androidx.appcompat.app.AlertDialog;
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
            if(delayIT.getText().length()!=0 || statusIT.getText().length()!=0 || commentIT.getText().length()!=0){
                if (commentIT.getText().length()<100){
                    CommunicationController.addPost(this, Model.getInstance().getSid(),did,
                            delayIT.getText().toString(),
                            statusIT.getText().toString(),
                            commentIT.getText().toString(),
                            response -> {
                                Log.d(MyStrings.VOLLEY, "Just added a post");
                                CommunicationController.retrievePosts(this, did, Model.getInstance().getPostAdapter());

                            },
                            error -> {
                                Log.d(MyStrings.VOLLEY, error.toString());
                            });

                    super.onBackPressed();
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle("Il commento è troppo lungo")
                            .setMessage("Scrivi un commento di massimo 100 caratteri")
                            .setNegativeButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }else {
                new AlertDialog.Builder(this)
                        .setTitle("Non è possibile pubblicare un commento vuoto")
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }


        });

    }
}