package edu.uoc.mapgame;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.uoc.mapgame.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button btnValidate;
    private Button btnSetPosition;
    private TextView txtQuestion;
    private Button btn_Next;
    LatLng selectedPos;

    Marker selectedMarker = null;
    int currentQuiz = -1;
    ArrayList<Quiz> quizArrayList = new ArrayList<Quiz>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //----------------
        //For testing purposes:
        quizArrayList.add(new Quiz("Where is Barcelona",2.205967369580916,41.49053769125906));
        quizArrayList.add(new Quiz("Where is Paris",2.3448274873948223, 48.862449322836355));
        //----------------

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
                selectedMarker = mMap.addMarker(new MarkerOptions().position(selectedPos).title("Your position"));
                btnValidate.setEnabled(true);
            }
        });
        btnValidate = findViewById(R.id.btn_validate);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                double lon = quizArrayList.get(currentQuiz).lon;
                double lat = quizArrayList.get(currentQuiz).lat;
                LatLng targetPos = new LatLng(lat,lon);
                mMap.addMarker(new MarkerOptions().position(targetPos).title("TARGET").icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)));

                Double distance = SphericalUtil.computeDistanceBetween(targetPos, selectedPos)/1000;

                txtQuestion.setText("Distance is: " + df.format(distance) +" km");
                Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
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
        double lon = quizArrayList.get(currentQuiz).lon;
        double lat = quizArrayList.get(currentQuiz).lat;
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
        btn_Next.setVisibility(View.INVISIBLE);
        currentQuiz++;
        btnValidate.setVisibility(View.VISIBLE);
        btnValidate.setEnabled(true);
        btnSetPosition.setVisibility(View.VISIBLE);
        btnSetPosition.setEnabled(true);
        txtQuestion.setText(quizArrayList.get(currentQuiz).description);

    }
}