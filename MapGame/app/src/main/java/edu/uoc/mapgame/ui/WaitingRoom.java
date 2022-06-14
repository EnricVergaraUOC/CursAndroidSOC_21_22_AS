package edu.uoc.mapgame.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uoc.mapgame.R;
import edu.uoc.mapgame.model.Level;
import edu.uoc.mapgame.model.Quiz;


public class WaitingRoom extends AppCompatActivity {

    String serverName = "";
    boolean isServer = false;
    Button btnStartServer;
    TextView txtListOfPlayers;
    String docID;
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


        btnStartServer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartParty();
            }
        });
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
                        docID = doc.getDocument().getId();
                        Map<String,Object> party = doc.getDocument().getData();
                        List<HashMap<String,Object>> players = (List<HashMap<String,Object>>) party.get("players");

                        String allPlayers = "";

                        for (HashMap<String,Object> player: players) {
                            String email =  (String) player.get("email");
                            allPlayers += email + "\n";
                            numPlayers++;
                        }
                        txtListOfPlayers.setText(allPlayers);

                        if (!isServer){
                            long nextQuestion = (long) party.get("readyToNextQuestion");
                            if (nextQuestion == 1){
                                LoadGame();
                            }
                        }
                    }
                }
            }
        });
    }

    void LoadGame(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("quiz");

        usersRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Level> levels = new ArrayList<Level>();
                            levels.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                List<Map<String, Object> > data_levels = (List<Map<String, Object> >) data.get("levels");
                                int index = 0;
                                for (Map<String, Object> level: data_levels) {
                                    String description = (String) level.get("description");
                                    Level newLevel = new Level(description, true);
                                    List<Map<String, Object> > questions = (List<Map<String, Object>>) level.get("questions");
                                    for (Map<String, Object> question: questions) {
                                        String quiz_description = (String) question.get("description");
                                        double quiz_lon = (double) question.get("lon");
                                        double quiz_lat = (double) question.get("lat");
                                        // public Quiz(String desc, double lon, double lat){
                                        Quiz newQuiz = new Quiz(quiz_description, quiz_lon, quiz_lat);
                                        newLevel.quizzes.add(newQuiz);
                                    }
                                    levels.add(newLevel);
                                }
                            }
                            Intent k = new Intent(WaitingRoom.this, MapsActivity.class);
                            k.putExtra("levelInfo",levels.get(0));
                            k.putExtra("multiplayerMode",true);
                            k.putExtra("isServer",isServer);
                            startActivity(k);
                        }
                    }
                });
    }




    void StartParty(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference party = db.collection("Multiplayer").document(docID);
        party.update("readyToNextQuestion", 1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        LoadGame();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("KAKA", "Error updating document", e);
                    }
                });


    }
}