package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class TresEnRaya extends AppCompatActivity {
    private static final String EMPTY = "";
    private ImageButton[][] board = new ImageButton[3][3];
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
                        ImageButton btn = (ImageButton) v;
                        int tag = (int)btn.getTag();
                        boolean winner = false;
                        if (tag == 0){
                            if (turnOfX){
                                btn.setImageResource(R.drawable.tictactoe_cell_x);
                                btn.setTag(1);
                            }else{
                                btn.setImageResource(R.drawable.tictactoe_cell_o);
                                btn.setTag(2);
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
        int tag = 0;
        do{
            randRow = Min + (int)(Math.random() * ((Max - Min) + 1));
            randCol = Min + (int)(Math.random() * ((Max - Min) + 1));

            tag = (int) board[randRow][randCol].getTag();
        }while(tag != 0);


        board[randRow][randCol].performClick();
    }


    private boolean CheckWinner(){
        boolean winner = false;
        int[][] status = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                status[i][j] = (int)board[i][j].getTag();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (status[i][0] == status[i][1]
                    && status[i][0] == status[i][2]
                    && status[i][0] != 0) {
                winner  = true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (status[0][i] == status[1][i]
                    && status[0][i] == status[2][i]
                    && status[0][i] != 0) {
                winner = true;
            }
        }
        if (status[0][0] == status[1][1]
                && status[0][0] == status[2][2]
                && status[0][0] != 0) {
            winner =  true;
        }

        if ( status[0][2] == status[1][1]
                && status[0][2] == status[2][0]
                && status[0][2] != 0){
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
                board[i][j].setImageResource(R.drawable.tictactoe_empty_cell);
                board[i][j].setTag(0);
            }
        }
    }

}