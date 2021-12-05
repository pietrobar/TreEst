package com.example.progettotreest;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PostsViewHolder extends RecyclerView.ViewHolder{
    private TextView postAuthorTV;
    private TextView delayTV;
    private TextView statusTV;
    private TextView commentTV;


    public PostsViewHolder(View itemView) {
        super(itemView);
        postAuthorTV = itemView.findViewById(R.id.post_author_tv);
        delayTV = itemView.findViewById(R.id.delay_tv);
        statusTV = itemView.findViewById(R.id.status_tv);
        commentTV = itemView.findViewById(R.id.comment_tv);

    }

    public void updateContent(Post post) {

        postAuthorTV.setText(post.getAuthorName());
        delayTV.setText(String.valueOf(post.getDelay()));
        statusTV.setText(String.valueOf(post.getStatus()));
        commentTV.setText(post.getComment());
    }
}
