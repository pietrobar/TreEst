package com.example.progettotreest;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostsViewHolder extends RecyclerView.ViewHolder{
    private TextView postAuthorTV;
    private TextView delayTV;
    private TextView statusTV;
    private TextView commentTV;
    private Button followUnfollowBtn;
    private TextView datetimeTV;


    Database db;
    private View view;

    public PostsViewHolder(View itemView) {
        super(itemView);
        postAuthorTV = itemView.findViewById(R.id.post_author_tv);
        delayTV = itemView.findViewById(R.id.delay_tv);
        statusTV = itemView.findViewById(R.id.status_tv);
        commentTV = itemView.findViewById(R.id.comment_tv);
        datetimeTV = itemView.findViewById(R.id.datetime_textView);
        followUnfollowBtn = itemView.findViewById(R.id.follow_unfolllow_btn);
        db = Model.getInstance().getDB();
        this.view=itemView;


    }

    public void updateContent(Post post) {

        postAuthorTV.setText(post.getAuthorName());
        delayTV.setText(String.valueOf(post.getDelay()));
        statusTV.setText(String.valueOf(post.getStatus()));
        commentTV.setText(post.getComment());
        datetimeTV.setText(post.getDatetime());

        //todo: per ogni post prendere l'autore e verificare se ho l'immagine più recente
        new Thread(() -> {
            List<User> users = db.getDao().getAll();
            view.post(()->{
                Log.d(MyStrings.PROVA, "AAAA "+ users);
            });
        }).start();


        //todo: controllare che non sia io l'autore del post e in caso togliere il bottone

        if (post.isFollowingAuthor()){
            followUnfollowBtn.setText("-");
        }else {
            followUnfollowBtn.setText("+");
        }

        followUnfollowBtn.setOnClickListener(v -> {
            if(!post.isFollowingAuthor()) {
                CommunicationController.follow(v.getContext(), Model.getInstance().getSid(), post.getAuthor(),
                        response -> {
                            Log.d(MyStrings.VOLLEY, "Follow uid: " + post.getAuthor() + " " + post.getAuthorName());
                            followUnfollowBtn.setText("-");
                        }, error -> {
                            Log.d(MyStrings.VOLLEY, "Error: " + error);
                        });
            }else {
                CommunicationController.unfollow(v.getContext(), Model.getInstance().getSid(), post.getAuthor(),
                        response -> {
                            Log.d(MyStrings.VOLLEY, "Unfollow uid: " + post.getAuthor() + " " + post.getAuthorName());
                            followUnfollowBtn.setText("+");
                        }, error -> {
                            Log.d(MyStrings.VOLLEY, "Error: " + error);
                        });

            }
            post.setFollowingAuthor(!post.isFollowingAuthor());
        });
    }
}
