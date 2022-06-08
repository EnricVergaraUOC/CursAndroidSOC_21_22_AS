package edu.uoc.mapgame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import edu.uoc.mapgame.R;
import edu.uoc.mapgame.Utilities.Utils;

import androidx.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    TextView inputEmail;
    TextView inputPwd;
    TextView txt_error;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            GoToMain();
        }

        setContentView(R.layout.activity_login);


        txt_error = findViewById(R.id.txt_error_login);
        txt_error.setVisibility(View.INVISIBLE);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_goto_register);
        inputPwd = findViewById(R.id.pwd_login);
        inputEmail= findViewById(R.id.email_login);

        SharedPreferences prefs = LoginActivity.this.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
        String lastEmailLogged = prefs.getString("last_login_email", null);
        if (lastEmailLogged != null){
            inputEmail.setText(lastEmailLogged);
        }

        //Add actions to the buttons:
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                txt_error.setVisibility(View.INVISIBLE);
                if (Utils.isEmptyTextView(inputEmail) || Utils.isEmptyTextView(inputPwd)){
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText("Email and pwd can not be empty");

                }else {
                    String email = inputEmail.getText().toString();
                    String pwd = inputPwd.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences( "general_settings",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        String email = inputEmail.getText().toString();
                                        editor.putString("last_login_email", email);
                                        editor.apply();

                                        GoToMain();
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

    public void GoToMain(){
        Intent k = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(k);
    }


}