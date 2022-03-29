package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText user;
    private EditText pwd;

    private TextView errorUser;
    private TextView errorPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button checkCredentials = findViewById(R.id.checkCredentials);
        user = findViewById(R.id.user);
        pwd = findViewById(R.id.pwd);

        errorUser = findViewById(R.id.error_user);
        errorPwd = findViewById(R.id.error_pwd);

        errorUser.setVisibility(View.INVISIBLE);
        errorPwd.setVisibility(View.INVISIBLE);

        checkCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_pwd = pwd.getText().toString();
                if (s_pwd.length() <= 5){
                    errorPwd.setVisibility(View.VISIBLE);
                    errorPwd.setText("El password debe estar formado por más de 5 caracteres");
                }else{
                    errorPwd.setVisibility(View.INVISIBLE);
                }
            }
        });




    }
}