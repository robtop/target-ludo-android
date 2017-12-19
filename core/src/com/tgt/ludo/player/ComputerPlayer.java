package com.tgt.ludo.player;

import java.util.ArrayList;
import java.util.List;

import com.tgt.ludo.player.action.Move;
import com.tgt.ludo.rules.RuleEngine;
import com.tgt.ludo.ui.LudoScreen;

public class ComputerPlayer extends Player{

	public ComputerPlayer(LudoScreen screen,RuleEngine ruleEngine) {
		super( ruleEngine);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Move play() {
		super.play();
		//System.out.println("Turn: "+this.color);
		List<Integer> list = rollDice();
		//System.out.println("Dice "+list.size());
		List<Move> moves = new ArrayList<Move>();
		for (Integer integer : list) {
			moves.addAll(ruleEngine.getValidMoves(this, integer));
		}
		if (moves.isEmpty()) {
			// skip turn
			return new Move(true);
		}
		
		return selectMove(moves);
	}


	protected Move selectMove(List<Move> moves){
		int move = (int) Math.floor(Math.random()*moves.size());
	
		return moves.get(move);
	}
	protected  List<Integer> rollDice() {
	
		List<Integer> rolls = new ArrayList<Integer>();
		int diceRole = ruleEngine.getSingleDiceRoll();
		rolls.add(diceRole);
		
		if(diceRole==6){
			//System.out.println("Got 6 ");
			rolls.addAll(rollDice());
		}
		return rolls;
	}

}
