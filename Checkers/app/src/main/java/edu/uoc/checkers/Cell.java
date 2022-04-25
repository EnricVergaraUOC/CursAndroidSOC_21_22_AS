package edu.uoc.checkers;


import android.widget.ImageButton;

public class Cell {
	
	public final static boolean WHITE = true;
	public final static boolean BLACK = false;
	
	private final static String white = "\u2591";
	private final static String black = "\u2588";
	
	public Cell(boolean colour, Piece piece){
		this.SetColour(colour);
		this.SetPiece(piece);
		
	}
	
	public void empty() {
		this.piece = Piece.EMPTY;
	}
	
	public Piece getPiece() {
		return this.piece;
	}
	
	public boolean hasPiece() {
		return (this.piece != Piece.EMPTY);
 
	}
	
	public void SetColour(boolean colour) {
		this.colour = colour;
	}
	
	public void SetPiece(Piece piece) {
		this.piece = piece;
	}
	
	public String toString() {
		String stringColour = colour? white: black;
		String stringPiece = " ";
		if (piece != Piece.EMPTY) {
			stringPiece = piece.toString();
		}

		return stringColour + stringPiece;
	}

	public void Render(ImageButton btn){
		if (piece != Piece.EMPTY) {
			piece.Render(btn);
		}else {
			btn.setImageResource(R.drawable.empty);
		}
	}

	private Piece piece;
	private boolean colour;
}



