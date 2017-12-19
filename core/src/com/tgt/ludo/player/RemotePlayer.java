package com.tgt.ludo.player;

import java.util.List;

import com.tgt.ludo.player.action.Move;
import com.tgt.ludo.rules.RuleEngine;
import com.tgt.ludo.ui.LudoScreen;

public class RemotePlayer extends Player {

	public RemotePlayer(LudoScreen screen,RuleEngine ruleEngine) {
		super(ruleEngine);
	}

	@Override
	public Move play() {
		//TODO: connect to server to get the move
		return null;
	}
  
	@Override
	public void setTurn(boolean turn) {
		super.setTurn(turn);
		//TODO: connect to server and tell remove player that its their turn
		
	}

	public List<Integer> rollDice() {
		//TODO: get roll dice from server
		return null;
	}
}
