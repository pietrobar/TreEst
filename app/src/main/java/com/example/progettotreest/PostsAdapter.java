package com.example.progettotreest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class PostsAdapter extends RecyclerView.Adapter<PostsViewHolder> {
    private LayoutInflater mInflater;
    private Context context;

    public PostsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.post_row, parent, false);
        return new PostsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getPostsSize();
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        Post post = Model.getInstance().getPost(position);
        holder.updateContent(post);
    }
}
