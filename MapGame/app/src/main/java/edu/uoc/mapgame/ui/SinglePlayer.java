package edu.uoc.mapgame.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import edu.uoc.mapgame.R;
import edu.uoc.mapgame.model.Level;
import edu.uoc.mapgame.ui.adapter.LevelListAdapter;

public class SinglePlayer extends AppCompatActivity {

    ArrayList<Level> levels = new ArrayList<Level>();
    LevelListAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        this.getSupportActionBar().setTitle("SinglePlayer");


        levels.add(new Level("Level1",true));
        levels.add(new Level("Level2",false));
        levels.add(new Level("Level3",false));
        levels.add(new Level("Level4",false));
        levels.add(new Level("Level5",false));



        RecyclerView recyclerView = findViewById(R.id.level_list);
        adapter = new LevelListAdapter(levels, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}