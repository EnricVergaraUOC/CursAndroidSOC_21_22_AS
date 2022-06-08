package edu.uoc.mapgame.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

        GetQuizFromFirebase();
    }


    public void GetQuizFromFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("quiz");

        usersRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                List<Map<String, Object> > data_levels = (List<Map<String, Object> >) data.get("levels");
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
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}