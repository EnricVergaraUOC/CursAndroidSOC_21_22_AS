package edu.uoc.mapgame.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.uoc.mapgame.R;
import edu.uoc.mapgame.model.Level;
import edu.uoc.mapgame.model.Quiz;
import edu.uoc.mapgame.ui.adapter.LevelListAdapter;

public class SinglePlayer extends AppCompatActivity {

    ArrayList<Level> levels = new ArrayList<Level>();
    LevelListAdapter adapter = null;
    public int lastLevelUnlocked = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        this.getSupportActionBar().setTitle("SinglePlayer");


        RecyclerView recyclerView = findViewById(R.id.level_list);
        adapter = new LevelListAdapter(levels, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ListeningUserInfoFromFirebase();

    }


    public void ListeningUserInfoFromFirebase(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("email", currentUser.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(SinglePlayer.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
                for(DocumentChange doc:value.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        Map<String,Object> trip = doc.getDocument().getData();

                        long kk = (long) trip.get("lastLevelUnlocked");
                        lastLevelUnlocked = (int) kk;
                        GetQuizFromFirebase();


                    }else if (doc.getType()==DocumentChange.Type.MODIFIED){
                        Map<String,Object> trip = doc.getDocument().getData();
                        long kk = (long) trip.get("lastLevelUnlocked");
                        lastLevelUnlocked = (int) kk;
                        GetQuizFromFirebase();

                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void GetQuizFromFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("quiz");

        usersRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            levels.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                List<Map<String, Object> > data_levels = (List<Map<String, Object> >) data.get("levels");
                                int index = 0;
                                for (Map<String, Object> level: data_levels) {
                                    String description = (String) level.get("description");
                                    boolean unlocked = false;
                                    if (index <= lastLevelUnlocked){
                                        unlocked = true;
                                    }
                                    index++;
                                    Level newLevel = new Level(description, unlocked);
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
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}