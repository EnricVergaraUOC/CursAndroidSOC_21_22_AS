package edu.uoc.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    TextView inputUserName;
    TextView inputPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_goto_register);
        inputPwd = findViewById(R.id.input_pwd);
        inputUserName = findViewById(R.id.input_username);

        //Add actions to the buttons:
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(LoginActivity.this, TripListActivity.class);
                startActivity(k);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(k);
            }
        });
    }
}