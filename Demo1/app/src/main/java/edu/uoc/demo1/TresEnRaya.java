package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TresEnRaya extends AppCompatActivity {
    private static final String EMPTY = "";
    private Button[][] board = new Button[3][3];
    private boolean turnOfX = false;
    private TextView userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tres_en_raya);

        userInfo = findViewById(R.id.userInfo);
        UpdateUserTurn();

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
                        boolean winner = false;
                        if (value.compareTo(EMPTY) == 0){
                            if (turnOfX){
                                btn.setText("X");
                            }else{
                                btn.setText("O");
                            }

                            winner = CheckWinner();
                        }
                        if (!winner){
                            turnOfX = !turnOfX;
                            UpdateUserTurn();
                        }

                    }
                });
            }
        }



        ResetBoard();
    }

    private void UpdateUserTurn(){
        if (turnOfX){
            userInfo.setText("Turno de las X");
        }else{
            userInfo.setText("Turno de las O");
        }
    }
    private boolean CheckWinner(){
        boolean winner = false;
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = board[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals(EMPTY)) {
                winner  = true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals(EMPTY)) {
                winner = true;
            }
        }


        if (winner){
            if (turnOfX) {
                userInfo.setText("Han guanyat les X");
            }else{
                userInfo.setText("Han guanyat les O");
            }
        }
        return winner;
    }

    private void ResetBoard(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                board[i][j].setText(EMPTY);
            }
        }
    }

}