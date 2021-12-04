package com.example.progettotreest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


public class LinesDirectionAdapter extends RecyclerView.Adapter<LinesDirectionViewHolder> {
    private LayoutInflater mInflater;

    public LinesDirectionAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public LinesDirectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.line_direction_row, parent, false);
        return new LinesDirectionViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getSize();
    }

    @Override
    public void onBindViewHolder(LinesDirectionViewHolder holder, int position) {
        Line line = Model.getInstance().get(position);
        holder.updateContent(line);
    }
}