package edu.uoc.checkers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    public static final int NUM_COLS_ROWS = 8;
    private ImageButton[][] renderBoard = new ImageButton[NUM_COLS_ROWS][NUM_COLS_ROWS];
    private TextView lblInfo;
    private ChessBoard logicBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        logicBoard = new ChessBoard();


        lblInfo = findViewById(R.id.lbl_info);
        for (int i = 0; i < NUM_COLS_ROWS; i++){
            for (int j = 0; j < NUM_COLS_ROWS; j++){
                String buttonID = "imageButton"+i+j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                renderBoard[i][j] = findViewById(resID);
                //renderBoard[i][j].setBackgroundResource(0);
                renderBoard[i][j].setTag(i*10+j);
                renderBoard[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = (int) v.getTag();
                        int y = (int)id/10;
                        int x = id % 10;
                        DoCellAction(x, y);
                    }
                });
            }
        }

        logicBoard.Render(renderBoard);
    }




    private void ResetBoard()
    {
        for (int i = 0; i < NUM_COLS_ROWS; i++){
            for (int j = 0; j < NUM_COLS_ROWS; j++){
                renderBoard[i][j].setImageResource(R.drawable.black_0001);
                renderBoard[i][j].setTag(0);
            }
        }
    }
    public void DoCellAction(int x, int y){
        lblInfo.setText("X: " + x + ", Y: " + y);
    }
}