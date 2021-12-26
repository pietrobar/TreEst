package com.example.progettotreest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        //get did from previous activity
        int did = getIntent().getIntExtra("did",0);


        //value container of spinners
        final int[] delayInfo = {-1};
        final int[] statusInfo = {-1};
        //setting spinners
        final Spinner[] delaySpinner = {findViewById(R.id.delay_input_text)};
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.delay, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        delaySpinner[0].setAdapter(adapter1);

        delaySpinner[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                delayInfo[0] =position-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner statusSpinner = findViewById(R.id.status_input_text);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.status, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter2);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusInfo[0] = position-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        EditText commentIT = findViewById(R.id.comment_input_text);

        findViewById(R.id.publish_btn).setOnClickListener(v -> {
            if(delayInfo[0]!=-1 || statusInfo[0]!=-1 || commentIT.getText().length()!=0){
                if (commentIT.getText().length()<100){
                    CommunicationController.addPost(this, Model.getInstance().getSid(),did,
                            delayInfo[0],
                            statusInfo[0],
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