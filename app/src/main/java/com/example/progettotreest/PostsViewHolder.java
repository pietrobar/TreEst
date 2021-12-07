package com.example.progettotreest;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PostsViewHolder extends RecyclerView.ViewHolder{
    private TextView postAuthorTV;
    private TextView delayTV;
    private TextView statusTV;
    private TextView commentTV;
    private Button followUnfollowBtn;


    public PostsViewHolder(View itemView) {
        super(itemView);
        postAuthorTV = itemView.findViewById(R.id.post_author_tv);
        delayTV = itemView.findViewById(R.id.delay_tv);
        statusTV = itemView.findViewById(R.id.status_tv);
        commentTV = itemView.findViewById(R.id.comment_tv);
        followUnfollowBtn = itemView.findViewById(R.id.follow_unfolllow_btn);


    }

    public void updateContent(Post post) {

        postAuthorTV.setText(post.getAuthorName());
        delayTV.setText(String.valueOf(post.getDelay()));
        statusTV.setText(String.valueOf(post.getStatus()));
        commentTV.setText(post.getComment());
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
