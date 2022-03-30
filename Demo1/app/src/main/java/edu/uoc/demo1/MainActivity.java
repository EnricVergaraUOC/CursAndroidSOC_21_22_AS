package edu.uoc.demo1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ACTIVITY2 = 0;

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
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("user", user.getText().toString());
                    intent.putExtra("pwd", pwd.getText().toString());
                    //startActivityForResult(intent, REQUEST_ACTIVITY2);

                    someActivityResultLauncher.launch(intent);
                }
            }
        });
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        float value = data.getFloatExtra("ResultData", -1);
                        checkCredentials.setText("Valor " + value);
                    }
                }
            });
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
        //return allIsCorrect;
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ACTIVITY2) {
            float value = data.getIntExtra("ResultData", -1);
            checkCredentials.setText("Valor " + value);
        }


    }
}