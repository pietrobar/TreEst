package com.example.progettotreest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Line selectedLine = (Line) getIntent().getSerializableExtra("selectedLine");
        Terminus selectedTerminus = (Terminus) getIntent().getSerializableExtra("selectedTerminus");
        TextView selectedDirectionTV = findViewById(R.id.selectedDirection_tv);
        selectedDirectionTV.setText("Linea: " + selectedLine.getTerminus1().getSname() +" - "+  selectedLine.getTerminus2().getSname() + " direzione " + selectedTerminus.getSname());

        //todo: inizializzare recyclerview


    }
}