package com.example.progettotreest;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class LinesDirectionViewHolder extends RecyclerView.ViewHolder {
    private TextView lineTV;
    private Button direction1Btn;
    private Button direction2Btn;
    public LinesDirectionViewHolder(View itemView) {
        super(itemView);
        lineTV = itemView.findViewById(R.id.line_tv);
        direction1Btn = itemView.findViewById(R.id.direction1_btn);
        direction2Btn = itemView.findViewById(R.id.direction2_btn);
    }
    public void updateContent(Line line) {
        lineTV.setText(line.getTerminus1().getSname() + " - " + line.getTerminus2().getSname());
        direction1Btn.setText(line.getTerminus1().getSname());
        direction2Btn.setText(line.getTerminus2().getSname());

    }
}