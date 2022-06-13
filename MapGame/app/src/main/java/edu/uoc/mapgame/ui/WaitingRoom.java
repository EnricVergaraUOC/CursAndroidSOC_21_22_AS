package edu.uoc.mapgame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.uoc.mapgame.R;


public class WaitingRoom extends AppCompatActivity {

    String serverName = "";
    boolean isServer = false;
    Button btnStartServer;
    TextView txtListOfPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serverName = extras.getString("ServerName");
            isServer = extras.getBoolean("CreateServer");
        }

        txtListOfPlayers = findViewById(R.id.listOfPlayers);
        btnStartServer = findViewById(R.id.btnStartServer);

        if (isServer){
            btnStartServer.setText("Start: "+ serverName);
        }else{
            btnStartServer.setVisibility(View.INVISIBLE);
        }
    }
}