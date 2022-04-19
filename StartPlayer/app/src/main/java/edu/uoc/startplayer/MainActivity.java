package edu.uoc.startplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MultitouchView myTouchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myTouchView = new MultitouchView(this);
        setContentView(myTouchView);
        //setContentView(R.layout.activity_main);
    }
}