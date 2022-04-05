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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {


    public final static String AI_ENABLED = "AI_ENABLED";

    private ImageView imgTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button register = findViewById(R.id.goRgister);
        Button tresEnRaya = findViewById(R.id.go3EnRaya);
        Switch switchIA = findViewById(R.id.switchIA);
        imgTitle = findViewById(R.id.imgTitle);
        //imgTitle.setImageResource(R.drawable.tictactoe_title);
        switchIA.setChecked(false);

        Button photo = findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, UserProfile.class);
                intent.putExtra(UserProfile.NEW_USER, false);
                intent.putExtra(UserProfile.EDIT_MODE, true);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tresEnRaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, TresEnRaya.class);
                intent.putExtra(AI_ENABLED, switchIA.isChecked());
                startActivity(intent);

            }
        });


    }



}