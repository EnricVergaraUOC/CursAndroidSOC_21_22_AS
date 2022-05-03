package edu.uoc.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TripListActivity extends AppCompatActivity {
    FloatingActionButton btnAddNewTrip;
    ImageButton btnViewProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        btnAddNewTrip = findViewById(R.id.btn_add_new_trip);
        btnViewProfile = findViewById(R.id.btn_vew_profile);

        
        //Add actions to the buttons:
        btnAddNewTrip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(TripListActivity.this, TripEditActivity.class);
                startActivity(k);
            }
        });

        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(TripListActivity.this, UserProfileActivity.class);
                startActivity(k);
            }
        });


        TripInfo[] myListData = new TripInfo[] {
                new TripInfo("", "(10/17/2021)", "Trip 1", 1),
                new TripInfo("", "(10/17/2021)", "Trip 2",2),
                new TripInfo("", "(10/17/2021)", "Trip 3",3),
                new TripInfo("", "(10/17/2021)", "Trip 4",4),
                new TripInfo("", "(10/17/2021)", "Trip 5",5),
                new TripInfo("", "(10/17/2021)", "Trip 6",6),
                new TripInfo("", "(10/17/2021)", "Trip 7",7),
                new TripInfo("", "(10/17/2021)", "Trip 8",8),
                new TripInfo("", "(10/17/2021)", "Trip 9",9),
                new TripInfo("", "(10/17/2021)", "Trip 10",10),
                new TripInfo("", "(10/17/2021)", "Trip 11",11),
                new TripInfo("", "(10/17/2021)", "Trip 12",12)
        };

        RecyclerView recyclerView = findViewById(R.id.trip_list);
        TripListAdapter adapter = new TripListAdapter(myListData, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}