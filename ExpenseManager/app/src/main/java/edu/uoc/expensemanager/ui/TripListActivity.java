package edu.uoc.expensemanager.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.HashMap;
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

        ListeningTripList();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(TripListActivity.this)
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

    public void ListeningTripList(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tripsRef = db.collection("trips");
        tripsRef.whereArrayContains("users", currentUser.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(TripListActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
                for(DocumentChange doc:value.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        Map<String,Object> trip = doc.getDocument().getData();
                        String date = (String) trip.get("date");
                        String description = (String) trip.get("description");
                        String image_url = (String) trip.get("img_url");
                        ArrayList users = (ArrayList) trip.get("users");
                        TripInfo newTrip = new TripInfo(image_url, date, description,doc.getDocument().getId(), users);
                        trips.add(newTrip);
                    }else if (doc.getType()==DocumentChange.Type.MODIFIED){
                        Map<String,Object> trip = doc.getDocument().getData();
                        String date = (String) trip.get("date");
                        String description = (String) trip.get("description");
                        String image_url = (String) trip.get("img_url");
                        ArrayList users = (ArrayList) trip.get("users");
                        TripInfo newTrip = new TripInfo(image_url, date, description,doc.getDocument().getId(), users);
                        int currentPosition = 0;
                        for(int i=0;i<trips.size();i++){
                            if (doc.getDocument().getId().equals(trips.get(i).tripID)){
                                currentPosition=i;
                                break;
                            }
                        }
                        trips.set(currentPosition,newTrip);

                    }else if (doc.getType()==DocumentChange.Type.REMOVED){
                        String id=doc.getDocument().getId();
                        int currentPosition=-1;
                        for(int i=0;i<trips.size();i++){
                            if (id.equals(trips.get(i).tripID)){
                                currentPosition=i;
                                break;
                            }
                        }
                        trips.remove(currentPosition);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}

/*

void tripsCollectionListing(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tripsRef = db.collection("trips");
        tripsRef.whereArrayContains("users", currentUser.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(TripListActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
                for(DocumentChange doc:value.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        Map<String,Object> map= new HashMap<>();
                        map=doc.getDocument().getData();
                        String date = map.get("date").toString();
                        String description = map.get("description").toString();
                        String urlPath = map.get("urlPath").toString();
                        String id=map.get("id").toString();
                        Trip newTrip = new Trip(urlPath, date, description,id);
                        trips.add(newTrip);
                    }else if (doc.getType()==DocumentChange.Type.MODIFIED){
                        everyID=true;
                        Map<String,Object> map= new HashMap<>();
                        map=doc.getDocument().getData();
                        String date = map.get("date").toString();
                        String description = map.get("description").toString();
                        String urlPath = map.get("urlPath").toString();
                        String id=map.get("id").toString();
                        for(int i=0;i<trips.size();i++){
                            if (trips.get(i).getId().equals("0")){
                                Trip newTrip = new Trip(urlPath, date, description,id);
                                trips.set(i,newTrip);
                                everyID=false;
                                break;
                            }
                        }
                        if (everyID){
                            int currentPosition=-1;
                            for(int i=0;i<trips.size();i++){
                                if (id.equals(trips.get(i).getId())){
                                    currentPosition=i;
                                    break;
                                }
                            }
                            Trip newTrip = new Trip(urlPath, date, description,id);
                            trips.set(currentPosition,newTrip);
                        }
                    }else if (doc.getType()==DocumentChange.Type.REMOVED){
                        Map<String,Object> map= new HashMap<>();
                        String id=doc.getDocument().getId();
                        int currentPosition=-1;
                        for(int i=0;i<trips.size();i++){
                            if (id.equals(trips.get(i).getId())){
                                currentPosition=i;
                                break;
                            }
                        }
                        trips.remove(currentPosition);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
 */