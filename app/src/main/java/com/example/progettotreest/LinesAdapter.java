package com.example.progettotreest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


public class LinesAdapter extends RecyclerView.Adapter<LinesViewHolder> {
    private LayoutInflater mInflater;
    private Context context;

    public LinesAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public LinesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.line_direction_row, parent, false);
        return new LinesViewHolder(view,context);
    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getSize();
    }

    @Override
    public void onBindViewHolder(LinesViewHolder holder, int position) {
        Line line = Model.getInstance().get(position);
        holder.updateContent(line);
    }
}