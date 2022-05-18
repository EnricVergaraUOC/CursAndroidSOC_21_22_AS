package edu.uoc.expensemanager.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.model.TripInfo;
import edu.uoc.expensemanager.ui.adapter.TripListAdapter;

public class TripListActivity extends AppCompatActivity {
    FloatingActionButton btnAddNewTrip;
    ImageButton btnViewProfile;
    ArrayList<TripInfo> trips = new ArrayList<TripInfo>();
    TripListAdapter adapter = null;
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



        RecyclerView recyclerView = findViewById(R.id.trip_list);
        adapter = new TripListAdapter(trips, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        DoConnection();

    }

    public void DoConnection(){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference citiesRef = db.collection("trips");
        citiesRef.whereArrayContains("users", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> trip = document.getData();
                                String date = (String) trip.get("date");
                                String description = (String) trip.get("description");
                                String image_url = (String) trip.get("img_url");
                                TripInfo newTrip = new TripInfo(image_url, date, description,document.getId());
                                trips.add(newTrip);
                            }
                            adapter.notifyItemInserted(0);
                        } else {
                            String msg_error = task.getException().toString();
                            Log.w("TripListActivity", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}