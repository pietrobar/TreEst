package com.example.progettotreest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;


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
            intent.putExtra("line", currentLine);
            intent.putExtra("did", currentLine.getTerminus1().getDid());
            //before starting the new activity I want to save the preferred terminus
            savePreference(context, currentLine, currentLine.getTerminus1().getDid());
            context.startActivity(intent);
        });
        direction2Btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, BoardActivity.class);
            intent.putExtra("line", currentLine);
            intent.putExtra("did", currentLine.getTerminus2().getDid());
            savePreference(context, currentLine, currentLine.getTerminus2().getDid());
            context.startActivity(intent);
        });
    }

    private void savePreference(Context context, Line line, int did) {
        //save into shared preferences the line and the did of the last selected direction
        Gson gson = new Gson();
        String l = gson.toJson(line);
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyStrings.PREFS, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("line", l);
        editor.putInt("did",did);
        editor.commit();
    }

    public void updateContent(Line line) {
        this.currentLine=line;
        lineTV.setText(line.getTerminus1().getSname() + " - " + line.getTerminus2().getSname());
        direction1Btn.setText(line.getTerminus1().getSname());
        direction2Btn.setText(line.getTerminus2().getSname());



    }
}