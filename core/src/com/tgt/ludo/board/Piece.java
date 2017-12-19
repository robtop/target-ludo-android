package com.tgt.ludo.board;

public class Piece {

	// keep a copy for convenience :(
	// private ModelInstance pieceInstance;
	private Board.COLOR color;

	// at rest
	private boolean rest;
	// at homesuares 
	private boolean homeSq;
	// main triangle home
	private boolean mainHome;
	private Square sittingSuare;
	private boolean shake;
	
	private boolean killed;
	private boolean jailed;
	private boolean toHome;
	//track how much it moved
	private int moveCount = 0;

	//after a kill 
	public void reset(){
		rest = false;
		homeSq = false;
		sittingSuare = null;
		shake = false;
		killed = false;
		jailed=false;
		moveCount=0;
	}
	
	public Board.COLOR getColor() {
		return color;
	}

	public void setColor(Board.COLOR color) {
		this.color = color;
	}

	public boolean isRest() {
		return rest;
	}

	public void setRest(boolean rest) {
		this.rest = rest;
	}

	public boolean isHomeSq() {
		return homeSq;
	}

	public void setHomeSq(boolean homeSq) {
		this.homeSq = homeSq;
	}

	public Square getSittingSuare() {
		return sittingSuare;
	}

	public void setSittingSuare(Square sittingSuare) {
		this.sittingSuare = sittingSuare;
	}

	public boolean isShake() {
		return shake;
	}

	public void setShake(boolean shake) {
		this.shake = shake;
	}

	public boolean isKilled() {
		return killed;
	}

	public void setKilled(boolean killed) {
		this.killed = killed;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}

	public boolean isJailed() {
		return jailed;
	}

	public void setJailed(boolean jailed) {
		this.jailed = jailed;
	}

	public boolean isToHome() {
		return toHome;
	}

	public void setToHome(boolean toHome) {
		this.toHome = toHome;
	}

	public boolean isMainHome() {
		return mainHome;
	}

	public void setMainHome(boolean mainHome) {
		this.mainHome = mainHome;
	}

}
