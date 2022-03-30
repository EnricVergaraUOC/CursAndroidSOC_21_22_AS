package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        String s_user = intent.getStringExtra("user");
        String s_pwd = intent.getStringExtra("pwd");
        Log.i("DEMO-MainActivity2", s_user + "/" + s_pwd);

        TextView info = findViewById(R.id.info);
        info.setText(s_user + "/" + s_pwd);

        Button finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                Float value = new Float(10.5);

                data.putExtra("ResultData", 10);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });

    }
}