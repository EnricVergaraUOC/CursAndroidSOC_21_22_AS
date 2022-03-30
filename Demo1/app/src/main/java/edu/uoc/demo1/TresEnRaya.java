package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TresEnRaya extends AppCompatActivity {
    private static final String EMPTY = "";
    private Button[][] board = new Button[3][3];
    private boolean turnOfX = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tres_en_raya);

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                String buttonID = "button"+i+j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                board[i][j] = findViewById(resID);
                board[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button btn = (Button) v;
                        String value = btn.getText().toString();
                        if (value.compareTo(EMPTY) == 0){
                            if (turnOfX){
                                btn.setText("X");
                            }else{
                                btn.setText("O");
                            }

                        }


                        turnOfX = !turnOfX;
                    }
                });
            }
        }



        ResetBoard();
    }

    private void ResetBoard(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                board[i][j].setText(EMPTY);
            }
        }
    }

}