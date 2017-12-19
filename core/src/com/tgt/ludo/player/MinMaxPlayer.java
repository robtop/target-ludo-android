package com.tgt.ludo.player;

import java.util.List;

import com.tgt.ludo.ai.AIutil;
import com.tgt.ludo.player.action.Move;
import com.tgt.ludo.rules.RuleEngine;
import com.tgt.ludo.ui.LudoScreen;

public class MinMaxPlayer extends ComputerPlayer {

	public MinMaxPlayer(LudoScreen screen, RuleEngine ruleEngine) {
		super(screen, ruleEngine);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Move selectMove(List<Move> moves) {
      Move bestMove = new Move(true);
      float prevBestWt =0;
		for (Move move : moves) {
			float newWt = AIutil.analyzeMove(ruleEngine,move);
           if(newWt>prevBestWt){
        	   prevBestWt = newWt;
        	   bestMove = move;
           }
		}
		
		return bestMove;
	}

	
}
