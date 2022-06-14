package edu.uoc.mapgame.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uoc.mapgame.R;

public class MultiPlayer extends AppCompatActivity {

    EditText lblServerName;
    Button createServer;
    Button joinToServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        createServer = findViewById(R.id.btnCreateServer);
        joinToServer = findViewById(R.id.btnJoinToServer);
        lblServerName = findViewById(R.id.txt_sererName);

        createServer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean allOk = CheckServerName();
                if (allOk){
                    CreateServer();
                }
            }
        });

        joinToServer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean allOk = CheckServerName();
                if (allOk){
                    JoinToServer();
                }
            }
        });
        this.getSupportActionBar().setTitle("MultiPlayer");
    }

    boolean CheckServerName (){
        boolean allIsOk;
        if (lblServerName.getText().toString().compareTo("") != 0){
            allIsOk = true;
        }else{
            allIsOk = false;
            new AlertDialog.Builder(MultiPlayer.this)
                    .setTitle("Server name can't be empty")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Nothing to do.
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        return allIsOk;
    }

    void CreateServer(){

        Map<String, Object> newParty = new HashMap<>();
        newParty.put("id", lblServerName.getText().toString());
        newParty.put("readyToNextQuestion", 0);
        Map<String, Object> newPlayer = new HashMap<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        newPlayer.put("email", currentUser.getEmail());
        newPlayer.put("score", 0);
        newPlayer.put("currentQuestion", 1);

        List<Object> players = new ArrayList<Object>();
        players.add(newPlayer);
        newParty.put("players", players);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Add a new document with a generated ID
        db.collection("Multiplayer")
                .add(newParty)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Go to WaitingRoom
                        Intent k = new Intent(MultiPlayer.this, WaitingRoom.class);
                        k.putExtra("ServerName", lblServerName.getText().toString());
                        k.putExtra("CreateServer", true);
                        startActivity(k);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.toString();
                        //ShowErrorMessage(error);
                    }
                });
    }

    public void JoinToServer(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("Multiplayer");

        usersRef.whereEqualTo("id", lblServerName.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                AddNewPlayer(document.getId());
                            }
                        } else {
                            String msg_error = task.getException().toString();
                            Log.w("TripListActivity", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void AddNewPlayer(String docID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference party = db.collection("Multiplayer").document(docID);
        // Atomically add a new region to the "regions" array field.

        Map<String, Object> newPlayer = new HashMap<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        newPlayer.put("email", currentUser.getEmail());
        newPlayer.put("score", 0);
        newPlayer.put("currentQuestion", 1);

        party.update("players", FieldValue.arrayUnion(newPlayer))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Intent k = new Intent(MultiPlayer.this, WaitingRoom.class);
                        k.putExtra("ServerName", lblServerName.getText().toString());
                        k.putExtra("CreateServer", false);
                        startActivity(k);
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