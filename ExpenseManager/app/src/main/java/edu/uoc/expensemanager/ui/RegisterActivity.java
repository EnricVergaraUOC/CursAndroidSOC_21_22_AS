package edu.uoc.expensemanager.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.Utilities.Utils;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    TextView inputUserName;
    TextView inputPwd;
    TextView txt_error;
    private FirebaseAuth mAuth;

    public void kk (){

    }
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
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText("Email and pwd can not be empty");

                }else {
                    mAuth.createUserWithEmailAndPassword(inputUserName.getText().toString(), inputPwd.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        finish();
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
    }
}