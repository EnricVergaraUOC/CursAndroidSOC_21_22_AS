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
    private TextView lblError;
    private ChessBoard logicBoard;
    private String originalPosition;
    private boolean currentTurn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        originalPosition = "";
        currentTurn = Piece.WHITE;

        logicBoard = new ChessBoard();


        lblInfo = findViewById(R.id.lbl_info);
        lblError = findViewById(R.id.lbl_error);
        lblError.setText("");

        UpdatePlayerTurn();

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

        InitBoard();

        logicBoard.Render(renderBoard);
    }




    private void InitBoard()
    {
        for (int i = 0; i < NUM_COLS_ROWS; i++){
            for (int j = 0; j < NUM_COLS_ROWS; j++){
                renderBoard[i][j].setBackgroundColor(R.drawable.black_0001);
                renderBoard[i][j].setTag(0);
            }
        }
    }

    private void UpdatePlayerTurn(){
        if (currentTurn == Piece.WHITE){
            lblInfo.setText("Es el turno de las BLANCAS");
        }else{
            lblInfo.setText("Es el turno de las NEGRAS");
        }

    }
    public void DoCellAction(int x, int y){

        //X va de 0-7 --> de "a"-"h"
        //Y va de 0-7 --> de 8-1
        //(7,7) --> "h1"
        String pos = convertCoordToString(x,y);
        if (originalPosition.compareTo("") == 0){
            originalPosition = pos;
        }else{
            try {
                if(logicBoard.movePiece(originalPosition, pos, currentTurn)){
                    currentTurn = !currentTurn;
                    UpdatePlayerTurn();
                    lblError.setText("");
                }
                logicBoard.Render(renderBoard);
                originalPosition = "";
            } catch (CheckersException e) {
                e.printStackTrace();
                originalPosition = "";
                lblError.setText(e.getMessage());
            }
        }
        //si posOrigen es vacio
        //  rellenar posorigen
        //caso contrario
        //  llamar a move del boardLogic
    }

    public String convertCoordToString(int x, int y){
        String aux = "";
        char coord1 = (char)('a' + x);
        aux += coord1;
        char coord2 = (char)('8' - y);
        aux += coord2;
        return aux;
    }
}