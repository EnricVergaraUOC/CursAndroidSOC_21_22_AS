package edu.uoc.mapgame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.uoc.mapgame.R;

public class MainActivity extends AppCompatActivity {

    Button singlePlayer;
    Button multiPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singlePlayer = findViewById(R.id.btnSinglePLayer);
        multiPlayer = findViewById(R.id.btnMultiPLayer);

        singlePlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, SinglePlayer.class);
                startActivity(k);
            }
        });

        multiPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, MultiPlayer.class);
                startActivity(k);
            }
        });
    }
}