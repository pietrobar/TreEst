package com.example.progettotreest;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class LinesDirectionViewHolder extends RecyclerView.ViewHolder {
    private TextView myTextView;
    public LinesDirectionViewHolder(View itemView) {
        super(itemView);
        myTextView = itemView.findViewById(R.id.lineDirectionItem_textView);
    }
    public void updateContent(String text) { myTextView.setText(text);
    } }