package edu.uoc.expensemanager.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.Utilities.DownLoadImageTask;
import edu.uoc.expensemanager.Utilities.Utils;
import edu.uoc.expensemanager.model.PayerInfo;
import edu.uoc.expensemanager.model.TripInfo;

public class TripEditActivity extends AppCompatActivity {

    ImageView tripImage;
    ImageButton btn_changeImage;
    EditText tripDesc;
    Button btn_selectDate;
    TextView txt_tripDate;
    String selectedDate;
    Button btn_save_trip;
    TextView txt_info_connection;
    Uri avatar = null;
    ProgressBar loading_save;
    TripInfo tripInfo;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private StorageReference mStorageRef;
    private String emailFromCurrentUser = "";

    boolean editMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_edit);
        //Get email from the current user:
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        List users = new ArrayList();
        emailFromCurrentUser = currentUser.getEmail();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        selectedDate = "none";
        loading_save = findViewById(R.id.loading_save);
        loading_save.setVisibility(View.INVISIBLE);
        btn_save_trip = findViewById(R.id.btn_save_trip);
        tripImage = findViewById(R.id.img_avatar);
        tripDesc = findViewById(R.id.input_tripDesc);
        btn_changeImage = findViewById(R.id.btn_changeImage);
        btn_selectDate = findViewById(R.id.btn_selectDate);
        txt_info_connection = findViewById(R.id.txt_info_connection);
        txt_info_connection.setVisibility(View.INVISIBLE);
        txt_tripDate = findViewById(R.id.txt_tripDate);
        txt_tripDate.setText("Fecha:  ----");

        btn_changeImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkAndRequestPermissions(TripEditActivity.this)){
                    chooseImage(TripEditActivity.this);
                }
            }
        });

        btn_save_trip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editMode){
                    UpdateTrip();
                }else{
                    CreateNewTrip();
                }

            }
        });
        btn_selectDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog mDlgDatePicker = new DatePickerDialog(TripEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = year + "-" + (monthOfYear + 1 < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                                + (dayOfMonth < 10 ? "0" : "") + dayOfMonth;
                        selectedDate = date;
                        txt_tripDate.setText("Fecha:  "+date);
                    }
                }, 2014, 1, 1);
                mDlgDatePicker.show();
            }
        });
        editMode = false;
        btn_save_trip.setText("Create");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tripInfo = extras.getParcelable("tripInfo");
            tripDesc.setText(tripInfo.description);
            txt_tripDate.setText("Fecha:  "+tripInfo.date);
            selectedDate = tripInfo.date;
            if (tripInfo.image_url != null && tripInfo.image_url.compareTo("") != 0){
                new DownLoadImageTask(tripImage).execute(tripInfo.image_url);
            }
            editMode = true;
            btn_save_trip.setText("Update");
        }
    }


    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    // function to check permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(TripEditActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(TripEditActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(TripEditActivity.this);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        avatar = data.getData();
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        tripImage.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        if (resultCode == RESULT_OK && data != null) {
                            avatar = data.getData();
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(
                                        this.getContentResolver(), avatar);

                            } catch (Exception e) {
                                // Manage exception ...
                            }

                            if (bitmap != null) {
                                tripImage.setImageBitmap(bitmap);
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void UploadImage(String docID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("images/trips/"+docID+".jpg");

        riversRef.putFile(avatar)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        UpdateTrip(docID, imageUrl);
                                    }

                                });
                                result.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ShowErrorStatus(e.toString());
                                    }

                                });
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        ShowErrorStatus(exception.toString());
                    }
                });
    }

    public void UpdateTrip(String docID, String url){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference tripRef = db.collection("trips").document(docID);

        tripRef
                .update("img_url", url)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        TripSavedSuccessfully();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ShowErrorStatus(e.toString());
                    }
                });
    }
    public void CreateNewTripOnFirebase(){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("description",tripDesc.getText().toString() );
        user.put("date", selectedDate);
        user.put("img_url", "");
        List users = new ArrayList();
        users.add(emailFromCurrentUser);

        user.put("users", users);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Add a new document with a generated ID
        db.collection("trips")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String doc_id = documentReference.getId();
                        Log.d("TripEditActivity", "DocumentSnapshot added with ID: " + doc_id);
                        if (avatar != null){
                            UploadImage(doc_id);
                        }else{
                            TripSavedSuccessfully();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.toString();
                        ShowErrorStatus(error);
                    }
                });
    }


    public void ShowErrorStatus(String message){
        txt_info_connection.setVisibility(View.VISIBLE);
        txt_info_connection.setTextColor(Color.RED);
        txt_info_connection.setText(message);
        loading_save.setVisibility(View.INVISIBLE);
        btn_save_trip.setEnabled(true);
    }

    public void TripSavedSuccessfully(){
        txt_info_connection.setVisibility(View.VISIBLE);
        txt_info_connection.setTextColor(Color.GREEN);
        txt_info_connection.setText("Trip saved successfully");
        loading_save.setVisibility(View.INVISIBLE);
        btn_save_trip.setEnabled(true);
    }

    public void CreateNewTrip(){
        //Check if there is information on description and date,
        if (Utils.isEmptyTextView(tripDesc) || Utils.isEmptyString(selectedDate)){
            ShowErrorStatus("Description and date fields are required");
        }else{
            btn_save_trip.setEnabled(false);
            loading_save.setVisibility(View.VISIBLE);
            txt_info_connection.setVisibility(View.INVISIBLE);

            CreateNewTripOnFirebase();
        }
    }

    public void UpdateTrip(){
        if (Utils.isEmptyTextView(tripDesc) || Utils.isEmptyString(selectedDate)){
            ShowErrorStatus("Description and date fields are required");
        }else{
            btn_save_trip.setEnabled(false);
            loading_save.setVisibility(View.VISIBLE);
            txt_info_connection.setVisibility(View.INVISIBLE);

            Map<String, Object> expense = new HashMap<>();

            expense.put("date",selectedDate);
            expense.put("description",tripDesc.getText().toString() );

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference expenseRef = db.collection("trips").document(tripInfo.tripID);
            // Add a new document with a generated ID
            expenseRef
                    .update(expense)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent data = new Intent();
                            tripInfo.date = selectedDate;
                            tripInfo.description = tripDesc.getText().toString();
                            data.putExtra("date",tripInfo.date);
                            data.putExtra("desc",tripInfo.description);
                            setResult(200, data);

                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ShowErrorStatus(e.toString());
                        }
                    });
        }
    }
}