package edu.uoc.mapgame.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uoc.mapgame.R;


public class WaitingRoom extends AppCompatActivity {

    String serverName = "";
    boolean isServer = false;
    Button btnStartServer;
    TextView txtListOfPlayers;
    int numPlayers = 0;
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

        ListeningMultiplayerFromFirebase();
    }


    public void ListeningMultiplayerFromFirebase(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference multiplayerRef = db.collection("Multiplayer");
        multiplayerRef.whereEqualTo("id", serverName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(WaitingRoom.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
                for(DocumentChange doc:value.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.MODIFIED ||
                            doc.getType()==DocumentChange.Type.ADDED){
                        numPlayers = 0;
                        Map<String,Object> party = doc.getDocument().getData();
                        List<HashMap<String,Object>> players = (List<HashMap<String,Object>>) party.get("players");

                        String allPlayers = "";

                        for (HashMap<String,Object> player: players) {
                            String email =  (String) player.get("email");
                            allPlayers += email + "\n";
                            numPlayers++;
                        }
                        txtListOfPlayers.setText(allPlayers);
                    }
                }

            }
        });
    }
}