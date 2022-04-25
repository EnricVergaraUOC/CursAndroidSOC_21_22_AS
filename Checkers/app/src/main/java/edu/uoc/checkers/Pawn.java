package edu.uoc.checkers;

import android.widget.ImageButton;

import java.util.ArrayList;

public class Pawn extends Piece {
	final String white_pawn = "\u25CB";
	final String black_pawn = "\u25CF";
	
	public Pawn(boolean colour) {
		super(colour);
		if (colour == WHITE) {
			TOTAL_WHITE_PIECES++;
		}else {
			TOTAL_BLACK_PIECES++;
		}
	}


	public void Render(ImageButton btn){
		if (colour == WHITE){
			btn.setImageResource(R.drawable.white_piece);
		}else{
			btn.setImageResource(R.drawable.black_piece);
		}
	}
	
	public String toString() {
		return this.colour? white_pawn: black_pawn;
	}
	
	public ArrayList<String> GetValidMoves (ChessBoard board, String position) {
		ArrayList<String> moves = new ArrayList<String>();
		
		int VerticalDir = 1;
		if (this.colour == Piece.BLACK) {
			VerticalDir = -1;
		}
		String validPos;
		validPos = GetValidMovesAux(board, position, VerticalDir, 1);
		if (validPos != null) {
			moves.add(validPos);
		}
		
		validPos = GetValidMovesAux(board, position, VerticalDir, -1);
		if (validPos != null) {
			moves.add(validPos);
		}
		
		return moves;
	}
	
	public String GetValidMovesAux (ChessBoard board, String position,
		int hDir, int vDir) {
		String res = null;
		
		int col_original = board.getCol(position);
		int row_original = board.getRow(position);
		
		//Move just one diagonal position
		int col_move, row_move;
		col_move = col_original + vDir;
		row_move = row_original + hDir;
		if (!IsInsideBoard(col_move, row_move)){
			return null;
		}
		String newPos = convertPosToString(col_move, row_move);
		Cell c = board.getCell(newPos);
		if (!c.hasPiece()) {
			res = newPos;
		}else if(c.getPiece().GetColour() != this.colour){
			col_move += vDir;
			row_move += hDir;
			if (!IsInsideBoard(col_move, row_move)){
				return null;
			}else {
				newPos = convertPosToString(col_move, row_move);
				c = board.getCell(newPos);
				if (!c.hasPiece()) {
					res = newPos;
				}
			}
		}
			
		return res;
	}
	
	
}
