package edu.uoc.mapgame;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import edu.uoc.mapgame.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button btnValidate;
    private Button btnSetPosition;
    private TextView txtQuestion;
    LatLng selectedPos;
    LatLng targetPos;
    Marker selectedMarker = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSetPosition = findViewById(R.id.btn_setPos);
        btnSetPosition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedPos = mMap.getCameraPosition().target;
                if (selectedMarker != null){
                    selectedMarker.remove();
                }
                selectedMarker = mMap.addMarker(new MarkerOptions().position(selectedPos).title("NEW POINT"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        });
        btnValidate = findViewById(R.id.btn_validate);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.addMarker(new MarkerOptions().position(targetPos).title("TARGET"));
                Double distance = SphericalUtil.computeDistanceBetween(targetPos, selectedPos)/1000;
                txtQuestion.setText("Distance is: " + distance);

            }
        });
        txtQuestion = findViewById(R.id.txtQuestion);
        txtQuestion.setText("Where is Sidney?");
        targetPos = new LatLng(-34, 151);
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

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}