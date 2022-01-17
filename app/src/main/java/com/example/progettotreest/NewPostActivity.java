package com.example.progettotreest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

                CommunicationController.addPost(this, Model.getInstance().getSid(),did,
                        delayInfo[0],
                        statusInfo[0],
                        commentIT.getText().toString(),
                        response -> {
                            Log.d(MyStrings.VOLLEY, "Just added a post");
                            LoadingDialog loadingDialog =new LoadingDialog(this);
                            loadingDialog.startLoadingDialog();
                            CommunicationController.getPosts(this, Model.getInstance().getSid(), did,
                                    res -> {
                                        handleRetrievePostResponse(res);
                                        loadingDialog.startLoadingDialog();
                                    }, error->{
                                        handleRetrievePostError(error);
                                        loadingDialog.dismissLoadingDialog();
                                    });
                            super.onBackPressed();

                        },
                        error -> {
                            Log.d(MyStrings.VOLLEY, error.toString());
                            new MaterialAlertDialogBuilder(this)
                                    .setTitle("Problemi di rete")
                                    .setMessage("Impossibile effettuare operazione")
                                    .setNegativeButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        });



            }else {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Non è possibile pubblicare un post vuoto")
                        .setPositiveButton(android.R.string.ok,null)
                        .show();
            }


        });

    }


    private void handleRetrievePostError(VolleyError error) {
        Log.d(MyStrings.VOLLEY,"Errore "+error);
        CommunicationController.connectionError(this,"Problema di connessione");

    }

    private void handleRetrievePostResponse(JSONObject response) {
        Model.getInstance().clearPosts();
        Log.d(MyStrings.VOLLEY,"Just Received posts: " + response.toString());
        JSONArray postsJson = null;
        try {
            postsJson = response.getJSONArray("posts");
            for(int i = 0; i < postsJson.length(); i++) {
                JSONObject post = postsJson.getJSONObject(i);

                String datetime =  post.getString("datetime");
                String subDate = datetime.substring(0,datetime.indexOf("."));
                Model.getInstance().addPost(new Post(
                        post.has("delay")?post.getInt("delay"):-1,
                        post.has("status")?post.getInt("status"):-1,
                        post.has("comment")?post.getString("comment"):"No Comment",
                        post.getBoolean("followingAuthor"),
                        subDate,
                        post.getString("authorName"),
                        post.getInt("pversion"),
                        post.getInt("author")));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Model.getInstance().sortPosts();
        Model.getInstance().getPostAdapter().notifyDataSetChanged();

    }

}