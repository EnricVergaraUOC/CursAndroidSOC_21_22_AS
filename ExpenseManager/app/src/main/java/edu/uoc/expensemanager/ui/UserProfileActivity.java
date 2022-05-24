package edu.uoc.expensemanager.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.Utilities.DownLoadImageTask;

public class UserProfileActivity extends AppCompatActivity {

    Button btnLogout;
    Button btnSave;
    ImageButton btn_changeImage;
    EditText txtUserName;
    ImageView imgAvatar;
    TextView txt_email;
    TextView txt_errorMessage;
    String userID = "";
    Uri avatar = null;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        btnLogout = findViewById(R.id.btn_logout);
        btnSave = findViewById(R.id.btn_save);
        txtUserName = findViewById(R.id.input_name);
        imgAvatar = findViewById(R.id.img_avatar);
        txt_email  = findViewById(R.id.txt_email);
        txt_errorMessage = findViewById(R.id.error_message);
        btn_changeImage = findViewById(R.id.btn_changeImage);
        btn_changeImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkAndRequestPermissions(UserProfileActivity.this)){
                    chooseImage(UserProfileActivity.this);
                }
            }
        });
        ShowErrorMessage("");

        //Add actions to the buttons:
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SaveOnFirebase();
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(UserProfileActivity.this, LoginActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        LoadDataFromFirebase();

    }


    public void LoadDataFromFirebase(){

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
                                userID = document.getId();
                                Map<String,Object> trip = document.getData();
                                String email = (String) trip.get("email");
                                txt_email.setText(email);
                                String name = (String) trip.get("userName");
                                if (name != null){
                                    txtUserName.setText(name);
                                }
                                String imgURL = (String) trip.get("imgURL");
                                if (imgURL != null && imgURL.compareTo("") != 0){
                                    new DownLoadImageTask(imgAvatar).execute(imgURL);
                                }

                            }
                        } else {
                            String msg_error = task.getException().toString();
                            ShowErrorMessage(msg_error);
                        }
                    }
                });
    }


    public void ShowSuccessMessage(String message){
        txt_errorMessage.setVisibility(View.VISIBLE);
        txt_errorMessage.setTextColor(Color.GREEN);
        txt_errorMessage.setText(message);
    }
    public void ShowErrorMessage(String message){
        txt_errorMessage.setVisibility(View.VISIBLE);
        txt_errorMessage.setTextColor(Color.RED);
        txt_errorMessage.setText(message);
    }

    public void SaveOnFirebase(){
        if (avatar != null){
            UploadImage(userID);
        }else{
            UpdateUser("");
        }


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
                if (ContextCompat.checkSelfPermission(UserProfileActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(UserProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(UserProfileActivity.this);
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
                        imgAvatar.setImageBitmap(selectedImage);
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
                                imgAvatar.setImageBitmap(bitmap);
                            }

                        }
                    }
                    break;
            }
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


    public void UploadImage(String docID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("images/users/"+userID+".jpg");

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
                                        UpdateUser(imageUrl);
                                    }

                                });
                                result.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ShowErrorMessage(e.toString());
                                    }

                                });
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        ShowErrorMessage(exception.toString());
                    }
                });
    }


    public void UpdateUser(String imageURL){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        DocumentReference userRef = usersRef.document(userID);
        userRef
                .update("userName", txtUserName.getText().toString(),"imgURL", imageURL)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ShowSuccessMessage("User updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ShowErrorMessage(e.toString());
                    }
                });
    }
}