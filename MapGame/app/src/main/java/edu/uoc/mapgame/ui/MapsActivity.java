package edu.uoc.mapgame.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uoc.mapgame.R;
import edu.uoc.mapgame.databinding.ActivityMapsBinding;
import edu.uoc.mapgame.model.Level;
import edu.uoc.mapgame.model.Quiz;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button btnValidate;
    private Button btnSetPosition;
    private TextView txtQuestion;
    private TextView txtTotalDistance;
    private Button btn_Next;
    LatLng selectedPos;
    float totalDistance = 0.0f;

    Marker selectedMarker = null;
    Marker target = null;
    Polyline polyline = null;
    int currentQuiz = -1;
    int lastLevelUnlocked = -1;
    Level currentLevel = null;
    int indexCurrentLevel = -1;
    boolean saveNewRecordOnFirebase = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currentLevel = getIntent().getParcelableExtra("levelInfo");
        lastLevelUnlocked = (int) getIntent().getIntExtra("lastLevelUnlocked", -1);
        indexCurrentLevel = (int) getIntent().getIntExtra("indexCurrentLevel", -1);
        //getActionBar().setTitle("Level: "+currentLevel.name);

        btnSetPosition = findViewById(R.id.btn_setPos);
        btnSetPosition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedPos = mMap.getCameraPosition().target;
                if (selectedMarker != null){
                    selectedMarker.remove();
                }
                selectedMarker = mMap.addMarker(new MarkerOptions().position(selectedPos).title("Your position"));
                btnValidate.setEnabled(true);
            }
        });
        btnValidate = findViewById(R.id.btn_validate);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                double lon = currentLevel.quizzes.get(currentQuiz).lon;
                double lat = currentLevel.quizzes.get(currentQuiz).lat;
                LatLng targetPos = new LatLng(lat,lon);
                target = mMap.addMarker(new MarkerOptions().position(targetPos).title("TARGET").icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)));

                Double distance = SphericalUtil.computeDistanceBetween(targetPos, selectedPos)/1000;

                txtQuestion.setText("Distance between target and selected: " + df.format(distance) +" km");
                totalDistance += distance;
                txtTotalDistance.setText("Total Distance: " + df.format(totalDistance) +" km");
                polyline = mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(targetPos, selectedPos));

                btnSetPosition.setVisibility(View.INVISIBLE);
                btnValidate.setVisibility(View.INVISIBLE);
                ShowAllMarkers();
                btn_Next.setVisibility(View.VISIBLE);

            }
        });
        btnValidate.setEnabled(false);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtTotalDistance = findViewById(R.id.textTotalDistance);
        btn_Next = findViewById(R.id.btn_Next);
        btn_Next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PrepareNextQuestion();
            }
        });
        PrepareNextQuestion();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

    }


    public void ShowAllMarkers (){
        double lon = currentLevel.quizzes.get(currentQuiz).lon;
        double lat = currentLevel.quizzes.get(currentQuiz).lat;
        LatLng targetPos = new LatLng(lat,lon);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //the include method will calculate the min and max bound.
        builder.include(selectedPos);
        builder.include(targetPos);

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.05); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        mMap.animateCamera(cu);
    }


    public void PrepareNextQuestion(){

        if (currentQuiz != -1){
            if (target!= null){
                target.remove();
                target = null;
            }
            if (selectedMarker != null){
                selectedMarker.remove();
                selectedMarker = null;
            }
            if (polyline != null){
                polyline.remove();
                polyline = null;
            }
        }

        if (currentQuiz == currentLevel.quizzes.size()-1){
            //Game finished.
            String msg_pass = "";
            if (lastLevelUnlocked == indexCurrentLevel){
                 msg_pass = "Congratulations you can go to the next level!";
                saveNewRecordOnFirebase = true;
            }else{
                msg_pass = "Congratulations!";
            }

            String msg_fail = "Sorry, you have to repeat the level with distance less than 2000 km";
            String msg = "";
            if (totalDistance < 2000){
                msg = msg_pass;
            }else{
                msg = msg_fail;
            }
            new android.app.AlertDialog.Builder(MapsActivity.this)
                    .setTitle("Round Finished")
                    .setMessage("Total distance is:  " +df.format(totalDistance) +" km.\n" + msg)

                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (saveNewRecordOnFirebase){
                                SaveNewRecordOnFirebase();
                            }else{
                                finish();
                            }

                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{
            txtTotalDistance.setText("Total Distance: " + df.format(totalDistance) +" km");
            btn_Next.setVisibility(View.INVISIBLE);
            currentQuiz++;
            btnValidate.setVisibility(View.VISIBLE);
            btnValidate.setEnabled(true);
            btnSetPosition.setVisibility(View.VISIBLE);
            btnSetPosition.setEnabled(true);
            txtQuestion.setText(currentLevel.quizzes.get(currentQuiz).description);
        }



    }

    public void SaveNewRecordOnFirebase(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userID = document.getId();

                                UpdateUserOnFirebase(userID);

                            }
                        } else {
                            String msg_error = task.getException().toString();
                            //ShowErrorMessage(msg_error);
                        }
                    }
                });
    }

    public void UpdateUserOnFirebase(String userID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userID);

        userRef
                .update("lastLevelUnlocked", lastLevelUnlocked+1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //ShowErrorStatus(e.toString());
                    }
                });
    }


}