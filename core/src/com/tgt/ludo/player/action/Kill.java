package com.tgt.ludo.player.action;

import java.util.List;

import com.tgt.ludo.board.Piece;

public class Kill {
	private List<Piece> killedPiece;
	private Piece killerPiece;

	
	public List<Piece> getKilledPiece() {
		return killedPiece;
	}

	public void setKilledPiece(List<Piece> killedPiece) {
		this.killedPiece = killedPiece;
	}

	public Piece getKillerPiece() {
		return killerPiece;
	}

	public void setKillerPiece(Piece killerPiece) {
		this.killerPiece = killerPiece;
	}

}
