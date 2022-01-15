package com.example.progettotreest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        TextView beginLabel = findViewById(R.id.from);
        TextView arriveLabel = findViewById(R.id.to);
        beginLabel.setText(line.getBegin(did));
        arriveLabel.setText(line.getArrive(did));

        RecyclerView recyclerView = findViewById(R.id.recyclerView_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PostsAdapter adapter = new PostsAdapter(this);
        recyclerView.setAdapter(adapter);

        Model.getInstance().setPostAdapter(adapter);
        this.adapter=adapter;
        this.did=did;

        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        CommunicationController.getPosts(this, Model.getInstance().getSid(), did,
                response -> {
                    handleRetrievePostResponse(response);
                    loadingDialog.dismissLoadingDialog();
                }, error->{
                    handleRetrievePostError(error);
                    loadingDialog.dismissLoadingDialog();
                });



        //DEFINE BUTTONS LISTENERS
        findViewById(R.id.floatingActionButton3).setOnClickListener(v -> {
            Intent intent = new Intent(this, NewPostActivity.class);
            intent.putExtra("did", this.did);
            startActivity(intent);
        });

        findViewById(R.id.switch_line_btn).setOnClickListener(v -> {
            SharedPreferences sharedPreferences = this.getSharedPreferences(MyStrings.PREFS, 0);
            int currentDid = sharedPreferences.getInt("did",-1);
            //invert terminus
            int newDid = line.getTerminus1().getDid()==currentDid ? line.getTerminus2().getDid() : line.getTerminus1().getDid();
            beginLabel.setText(line.getBegin(newDid));
            arriveLabel.setText(line.getArrive(newDid));
            //save new did
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("did",newDid);
            editor.commit();
            this.did=newDid;
            loadingDialog.startLoadingDialog();
            CommunicationController.getPosts(this, Model.getInstance().getSid(), newDid,
                    response -> {
                        handleRetrievePostResponse(response);
                        loadingDialog.dismissLoadingDialog();
                    }, error->handleRetrievePostError(error));
        });

        findViewById(R.id.details_btn).setOnClickListener(v ->{
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("did",this.did);
            startActivity(intent);
        });

        findViewById(R.id.imageButton).setOnClickListener(v->{
            Intent intent = new Intent(this, ProfileSettingsActivity.class);
            startActivity(intent);
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
                        post.has("comment")?post.getString("comment"):"",
                        post.getBoolean("followingAuthor"),
                        subDate,
                        post.getString("authorName"),
                        post.getInt("pversion"),
                        post.getInt("author")));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Model.getInstance().sortPosts();
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //I want to update the posts content whenever i get back from another activity
        if (this.adapter!=null && this.did!=-1){
            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.startLoadingDialog();
            CommunicationController.getPosts(this, Model.getInstance().getSid(), this.did,
                    response -> {
                        handleRetrievePostResponse(response);
                        loadingDialog.dismissLoadingDialog();
                    }, error->{
                        handleRetrievePostError(error);
                        loadingDialog.dismissLoadingDialog();
                    });

        }

    }
}