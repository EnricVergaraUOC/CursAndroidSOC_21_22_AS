package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    public static final String EDIT_MODE = "USER_PROFILE_EDIT_MODE";
    public static final String NEW_USER = "USER_PROFILE_NEW_USER";

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    private static final int INFO_USER_NAME = 0;
    private static final int INFO_USER_EMAIL = 1;
    private static final int INFO_USER_PICTURE = 2;
    private static final String KEY_INFO_USER_NAME = "INFO_USER_NAME";
    private static final String KEY_INFO_EMAIL = "INFO_EMAIL";
    private static final String KEY_INFO_PICTURE = "INFO_PICTURE ";
    private static final String ID_USER_PROFILE = "ID_USER_PROFILE";

    private ImageView userImage;
    private ImageButton btn_changeAvatar;
    private Button btn_save;
    private EditText editText_userName;
    private EditText editText_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();
        Boolean editMode = intent.getBooleanExtra(EDIT_MODE, false);
        Boolean newUser = intent.getBooleanExtra(NEW_USER, false);
        btn_changeAvatar = findViewById(R.id.changeUsePhoto);
        btn_save = findViewById(R.id.btn_save);
        editText_userName = findViewById(R.id.edit_user_name);
        editText_email = findViewById(R.id.edit_user_email);
        userImage = findViewById(R.id.userImage);

        setEditMode(editMode || newUser);

        if (!newUser){
            //get data from info provider (webservice, from disk)...
            String[] userInfo = new String[3];

            getUserInfo(userInfo);
            setInfoOnWidgets(userInfo);
        }

        btn_changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions(UserProfile.this)){
                    chooseImage(UserProfile.this);
                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences(ID_USER_PROFILE,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();
                editor.putString(KEY_INFO_USER_NAME, editText_userName.getText().toString());
                editor.putString(KEY_INFO_EMAIL, editText_email.getText().toString());

                //Convert from bitmap to base64 string
                BitmapDrawable drawable = (BitmapDrawable) userImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                editor.putString(KEY_INFO_PICTURE, encoded);

                editor.commit();
            }
        });



    }

    private void getUserInfo(String[] userInfo){
        SharedPreferences pref = getSharedPreferences(ID_USER_PROFILE, Context.MODE_PRIVATE);

        userInfo[INFO_USER_NAME] = pref.getString(KEY_INFO_USER_NAME,"");;
        userInfo[INFO_USER_EMAIL] = pref.getString(KEY_INFO_EMAIL,"");;
        userInfo[INFO_USER_PICTURE] = pref.getString(KEY_INFO_PICTURE,"");;
    }
    private void setInfoOnWidgets(String[] userInfo) {
        editText_userName.setText(userInfo[INFO_USER_NAME]);
        editText_email.setText(userInfo[INFO_USER_EMAIL]);
        if (userInfo[INFO_USER_PICTURE].compareTo("") != 0){
            byte[] decodedString = Base64.decode(userInfo[INFO_USER_PICTURE], Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            userImage.setImageBitmap(decodedByte);
        }

    }

    private void setEditMode(boolean editMode){
        if (!editMode){
            btn_changeAvatar.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.INVISIBLE);
            editText_userName.setEnabled(false);
            editText_email.setEnabled(false);
        }else{
            btn_changeAvatar.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            editText_userName.setEnabled(true);
            editText_email.setEnabled(true);
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
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                if (ContextCompat.checkSelfPermission(UserProfile.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(UserProfile.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(UserProfile.this);
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
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        userImage.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                userImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }
}