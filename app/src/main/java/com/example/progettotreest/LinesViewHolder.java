package com.example.progettotreest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class LinesViewHolder extends RecyclerView.ViewHolder {
    private Line currentLine;
    private TextView lineTV;
    private Button direction1Btn;
    private Button direction2Btn;
    public LinesViewHolder(View itemView, Context context) {
        super(itemView);
        lineTV = itemView.findViewById(R.id.line_tv);
        direction1Btn = itemView.findViewById(R.id.direction1_btn);
        direction2Btn = itemView.findViewById(R.id.direction2_btn);

        direction1Btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, BoardActivity.class);
            intent.putExtra("selectedTerminus", currentLine.getTerminus1());
            intent.putExtra("selectedLine", currentLine);
            context.startActivity(intent);
        });
        direction2Btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, BoardActivity.class);
            intent.putExtra("selectedTerminus", currentLine.getTerminus2());
            intent.putExtra("selectedLine", currentLine);
            context.startActivity(intent);
        });
    }
    public void updateContent(Line line) {
        this.currentLine=line;
        lineTV.setText(line.getTerminus1().getSname() + " - " + line.getTerminus2().getSname());
        direction1Btn.setText(line.getTerminus1().getSname());
        direction2Btn.setText(line.getTerminus2().getSname());



    }
}