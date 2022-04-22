package edu.uoc.checkers;

import android.widget.ImageButton;

import java.util.ArrayList;

public class Queen extends Piece {
	final String white_queen = "\u25CE";
	final String black_queen = "\u25C9";
	
	public Queen(boolean colour) {
		super(colour);
	}


	public void Render(ImageButton btn){
		if (colour == WHITE){
			btn.setImageResource(R.drawable.white_0012);
		}else{
			btn.setImageResource(R.drawable.black_0012);
		}
	}
	public String toString() {
		return this.colour? white_queen: black_queen;
	}
	
	public  ArrayList<String> GetValidMoves (ChessBoard board, String position) {
		return null;
	}
}