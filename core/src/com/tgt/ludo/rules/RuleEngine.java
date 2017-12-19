package com.tgt.ludo.rules;

import java.util.List;

import com.tgt.ludo.board.Board;
import com.tgt.ludo.board.Piece;
import com.tgt.ludo.board.Square;
import com.tgt.ludo.player.Player;
import com.tgt.ludo.player.action.Kill;
import com.tgt.ludo.player.action.Move;

/**
 * Engine to enfocre rule of ludo.
 * 
 * @author robin
 *
 */
public interface RuleEngine {

	public boolean validMove(Piece piece,int diceVal);
	
	public List<Move> getValidMoves(Player player,int diceVal);
	
	public  int getSingleDiceRoll();
	
	public  Kill getKills();
	
	public Piece getPieceOnHomeSquare();
	
	public  Piece getPieceOnJail();
	
	//scenarios that can be used by ANN
	public boolean goToJail(Move move);
	
	public boolean goToHomeSquare(Move move);
	
	public boolean makeAkill(Move move);
	
	public boolean closeToKill(Move move);
	
	public boolean jumpJail(Move move);
	
	public boolean escapeKill(Move move);
	
	public boolean reachHome(Move move);

	//how many dice to give for a throw
	public int dicePerGame();
	
}
