package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText user;
    private EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button checkCredentials = findViewById(R.id.checkCredentials);
        user = findViewById(R.id.user);
        pwd = findViewById(R.id.pwd);
        checkCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_pwd = pwd.getText().toString();
                if (s_pwd.length() <= 5){
                    Toast.makeText(getApplicationContext(), "Error: el pwd debe ser como mínimo de 6 caracteres", Toast.LENGTH_LONG).show();//display the text of button1
                }


            }
        });




    }
}