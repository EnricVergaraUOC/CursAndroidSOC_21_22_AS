package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText user;
    private EditText pwd;

    private TextView errorUser;
    private TextView errorPwd;
    private Button checkCredentials;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCredentials = findViewById(R.id.checkCredentials);
        user = findViewById(R.id.user);
        pwd = findViewById(R.id.pwd);

        errorUser = findViewById(R.id.error_user);
        errorPwd = findViewById(R.id.error_pwd);

        errorUser.setVisibility(View.INVISIBLE);
        errorPwd.setVisibility(View.INVISIBLE);

        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    CheckUser();
                }
            }
        });

        pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    CheckPwd();
                }
            }
        });

        checkCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckPwd() && CheckUser() ){
                    //TODO.. connect to webservice or go to another view
                }
            }
        });

    }
    private boolean CheckPwd(){
        String s_pwd = pwd.getText().toString();


        char[] chars = s_pwd.toCharArray();
        boolean containsDigit = false;
        for(char c : chars){
            if(Character.isDigit(c)){
                containsDigit = true;
            }
        }
        //Check pwd:
        boolean allIsCorrect = false;
        if (s_pwd.length() <= 5){
            errorPwd.setVisibility(View.VISIBLE);
            errorPwd.setText("El password debe estar formado por más de 5 caracteres");
        }else if (!containsDigit) {
            errorPwd.setVisibility(View.VISIBLE);
            errorPwd.setText("El password debe contener al menos un número");
        } else{
            errorPwd.setVisibility(View.INVISIBLE);
            allIsCorrect = true;
        }

        return allIsCorrect;
    }
    private boolean CheckUser(){
        boolean allIsCorrect = false;
        //Check user email:
        String s_user = user.getText().toString();
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s_user);
        if (!m.matches()) {
            errorUser.setVisibility(View.VISIBLE);
            errorUser.setText("Debe ser un email con formato válido!");
        } else{
            errorUser.setVisibility(View.INVISIBLE);
            allIsCorrect = true;
        }
        return allIsCorrect;
    }
}