package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class TresEnRaya extends AppCompatActivity {

    Button[][] myButton  = new Button[3][3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tres_en_raya);

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                String buttonID = "button"+i+j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                myButton[i][j] = findViewById(resID);
            }
        }

    }

}