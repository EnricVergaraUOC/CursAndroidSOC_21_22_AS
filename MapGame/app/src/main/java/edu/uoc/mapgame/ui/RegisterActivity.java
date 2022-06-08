package edu.uoc.mapgame.ui;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.uoc.mapgame.R;
import edu.uoc.mapgame.Utilities.Utils;


public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    TextView inputUserName;
    TextView inputPwd;
    TextView txt_error;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btn_register);
        inputPwd = findViewById(R.id.input_pwd);
        inputUserName = findViewById(R.id.input_username);
        txt_error = findViewById(R.id.txt_error);
        txt_error.setVisibility(View.INVISIBLE);
        //Add actions to the buttons:
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                txt_error.setVisibility(View.INVISIBLE);
                if (Utils.isEmptyTextView(inputUserName) || Utils.isEmptyTextView(inputPwd)){
                    ShowErrorMessage("Email and pwd can not be empty");

                }else {
                    mAuth.createUserWithEmailAndPassword(inputUserName.getText().toString(), inputPwd.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        CreateNewUser();

                                    } else {
                                        String errorMessage = task.getException().toString();
                                        ShowErrorMessage(errorMessage);
                                    }
                                }
                            });
                }
            }
        });
    }

    public void CreateNewUser(){
        Map<String, Object> user = new HashMap<>();
        user.put("email", inputUserName.getText().toString());
        user.put("lastLevelUnlocked", 0);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.toString();
                        ShowErrorMessage(error);
                    }
                });


    }

    public void ShowErrorMessage(String msg){
        txt_error.setVisibility(View.VISIBLE);
        txt_error.setText(msg);
    }
}