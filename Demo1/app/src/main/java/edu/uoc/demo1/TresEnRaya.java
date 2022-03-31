package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TresEnRaya extends AppCompatActivity {
    private static final String EMPTY = "";
    private Button[][] board = new Button[3][3];
    private boolean turnOfX = false;
    private TextView userInfo;
    private boolean modeIA = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tres_en_raya);

        Intent intent = getIntent();
        modeIA = intent.getBooleanExtra(Menu.AI_ENABLED, false);

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
                            if (turnOfX && modeIA){
                                UpdateAI();
                            }
                        }
                    }
                });
            }
        }
        ResetBoard();
    }

    private void UpdateUserTurn(){
        if (turnOfX){
            if (modeIA){
                userInfo.setText("Turno de las X (IA)");
            }else{
                userInfo.setText("Turno de las X");
            }

        }else{
            userInfo.setText("Turno de las O");
        }
    }

    private void UpdateAI(){
        int Min = 0;
        int Max = 2;

        int randRow;
        int randCol;
        do{
            randRow = Min + (int)(Math.random() * ((Max - Min) + 1));
            randCol = Min + (int)(Math.random() * ((Max - Min) + 1));

        }while(board[randRow][randCol].getText().toString().compareTo(EMPTY) != 0);


        board[randRow][randCol].performClick();
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
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals(EMPTY)) {
            winner =  true;
        }

        if ( field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals(EMPTY)){
            winner =  true;
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