package com.tgt.ludo.player;

import java.util.ArrayList;
import java.util.List;

import com.tgt.ludo.board.Board.COLOR;
import com.tgt.ludo.board.Dice;
import com.tgt.ludo.board.Piece;
import com.tgt.ludo.player.action.Move;
import com.tgt.ludo.rules.RuleEngine;
import com.tgt.ludo.ui.BoardRenderer;
import com.tgt.ludo.ui.LudoScreen;

public abstract class Player {

	private boolean turn = false;
	protected List<Piece> pieces;
	protected RuleEngine ruleEngine;
	protected COLOR color;
	protected int startIndex;
	protected Piece selectedPiece;
	// green get 0,0, Yellow 0,1 , Red 1,1 , Blue 0,1 - used for dice placement
	// and start calculation
	protected int locX;
	protected int locY;

	protected boolean diceRolled = false;
	
	//if multiple dice
	protected boolean selectDice = false;

	public Player(RuleEngine ruleEngine) {
		this.ruleEngine = ruleEngine;
	}

	// Extending Players should set this
	protected List<Integer> diceRolls = new ArrayList<Integer>();

	// main game loop
	public Move play() {
		if (diceRolled) {
			// rollDice();
		}
		return null;
	}

	public boolean isTurn() {
		return turn;
	}

	/**
	 * Roll a single dice
	 * 
	 * @param dice
	 *            - The dice to be rolled
	 * @param boardRenderer
	 *            - to get the diceList and create new die if we get a six
	 * @return - list of dice values or null if we need another throw
	 */
	protected List<Integer> rollDice(Dice dice, BoardRenderer boardRenderer) {
		List<Dice> diceList = boardRenderer.getDiceList();
		int value = ruleEngine.getSingleDiceRoll();
		dice.setDiceValue(value);
		dice.setRolled(true);
		dice.setShake(false);
		if (value == 6) {
			Dice newdice = boardRenderer.createDiceInstance();
			newdice.setShake(true);
			diceList.add(newdice);
		} else {
			List<Integer> list = new ArrayList<Integer>();
			for (Dice diceTemp : diceList) {
				list.add(diceTemp.getDiceValue());
			}
			return list;
		}
		dice.setRolled(true);
		return null;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public List<Piece> getPieces() {
		return pieces;
	}

	public void setPieces(List<Piece> pieces) {
		this.pieces = pieces;
	}

	public RuleEngine getRuleEngine() {
		return ruleEngine;
	}

	public void setRuleEngine(RuleEngine ruleEngine) {
		this.ruleEngine = ruleEngine;
	}

	public boolean isDiceRolled() {
		return diceRolled;
	}

	public void setDiceRolled(boolean diceRolled) {
		this.diceRolled = diceRolled;
	}

	public List<Integer> getDiceRolls() {
		return diceRolls;
	}

	public void setDiceRolls(List<Integer> diceRolls) {
		this.diceRolls = diceRolls;
	}

	public COLOR getColor() {
		return color;
	}

	public void setColor(COLOR color) {
		this.color = color;
		switch (color) {
		case YELLOW:
			locX = 1;
			locY = 0;
			break;

		case RED:
			locX = 1;
			locY = 1;
			break;
		case BLUE:
			locX = 0;
			locY = 1;
			break;
		}
	}

	public int getLocX() {
		return locX;
	}

	public void setLocX(int locX) {
		this.locX = locX;
	}

	public int getLocY() {
		return locY;
	}

	public void setLocY(int locY) {
		this.locY = locY;
	}

	public Piece getSelectedPiece() {
		return selectedPiece;
	}

	public void setSelectedPiece(Piece selectedPiece) {
		this.selectedPiece = selectedPiece;
	}

	public boolean isSelectDice() {
		return selectDice;
	}

	public void setSelectDice(boolean selectDice) {
		this.selectDice = selectDice;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	@Override
	public String toString(){
		return super.toString().replaceAll("com.tgt.ludo.player", "");
	}

}
