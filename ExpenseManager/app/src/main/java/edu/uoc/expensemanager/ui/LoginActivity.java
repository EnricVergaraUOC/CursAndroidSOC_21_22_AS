package edu.uoc.expensemanager.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.Utilities.Utils;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    TextView inputUserName;
    TextView inputPwd;
    TextView txt_error;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            GoToTripList();
        }

        setContentView(R.layout.activity_login);


        txt_error = findViewById(R.id.txt_error_login);
        txt_error.setVisibility(View.INVISIBLE);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_goto_register);
        inputPwd = findViewById(R.id.input_pwd);
        inputUserName = findViewById(R.id.input_username);

        
        //Add actions to the buttons:
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txt_error.setVisibility(View.INVISIBLE);
                if (Utils.isEmpty(inputUserName) || Utils.isEmpty(inputPwd)){
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText("Email and pwd can not be empty");

                }else {
                    String email = inputUserName.getText().toString();
                    String pwd = inputPwd.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        GoToTripList();
                                    } else {
                                        String errorMessage = task.getException().toString();
                                        txt_error.setVisibility(View.VISIBLE);
                                        txt_error.setText(errorMessage);
                                    }
                                }
                            });
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(k);
            }
        });
    }

    public void GoToTripList(){
        Intent k = new Intent(LoginActivity.this, TripListActivity.class);
        startActivity(k);
    }

    public void DoConnection(){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String doc_id = documentReference.getId();
                        Log.d("TripEditActivity", "DocumentSnapshot added with ID: " + doc_id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.toString();
                        Log.w("TripEditActivity", "Error adding document", e);
                    }
                });
    }

}