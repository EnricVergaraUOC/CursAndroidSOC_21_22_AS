package edu.uoc.mapgame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.uoc.mapgame.R;

public class SinglePlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        this.getSupportActionBar().setTitle("SinglePlayer");
    }
}