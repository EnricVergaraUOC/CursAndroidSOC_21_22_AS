package edu.uoc.mapgame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import edu.uoc.mapgame.R;

public class MainActivity extends AppCompatActivity {

    Button singlePlayer;
    Button multiPlayer;
    Button btn_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singlePlayer = findViewById(R.id.btnSinglePLayer);
        multiPlayer = findViewById(R.id.btnMultiPLayer);
        btn_logout =  findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure you want to sign out?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Nothing to do.
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

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