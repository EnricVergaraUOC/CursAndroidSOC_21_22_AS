package edu.uoc.expensemanager.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.Utils;

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

}