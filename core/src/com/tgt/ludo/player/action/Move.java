package com.tgt.ludo.player.action;

import com.tgt.ludo.board.Piece;

public class Move {

	private Piece piece;
	
	//move the piece by this many squares
	private int squares;
	//move the piece to start
    private boolean skipTurn;
    
    //signifies we need another move to complet the players turn
    private boolean incomplete;
    private int moveCount = 0;
    
    public Move(boolean skipTurn){
    	this.skipTurn = skipTurn;
    }
    public Move(Piece piece){
    	this.piece = piece;
    }
    
	public Move(Piece piece, int squares) {
		this.piece = piece;
		this.squares = squares;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public int getSquares() {
		return squares;
	}

	public void setSquares(int squares) {
		this.squares = squares;
	}


	public boolean isSkipTurn() {
		return skipTurn;
	}

	public void setSkipTurn(boolean skipTurn) {
		this.skipTurn = skipTurn;
	}
	public boolean isIncomplete() {
		return incomplete;
	}
	public void setIncomplete(boolean incomplete) {
		this.incomplete = incomplete;
	}
}
