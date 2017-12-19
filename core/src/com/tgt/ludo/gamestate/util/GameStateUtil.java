package com.tgt.ludo.gamestate.util;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.tgt.ludo.board.Board;
import com.tgt.ludo.board.Board.COLOR;
import com.tgt.ludo.player.ANNPlayer;
import com.tgt.ludo.player.ComputerPlayer;
import com.tgt.ludo.player.HumanPlayer;
import com.tgt.ludo.player.MinMaxPlayer;
import com.tgt.ludo.player.Player;
import com.tgt.ludo.rules.RuleEngine;
import com.tgt.ludo.ui.LudoScreen;

/***
 * Util class supporting the Game state controllers
 * 
 * @author robin
 *
 */
public class GameStateUtil {
	
public static  List<Player> createPlayers(Screen screen,RuleEngine ruleEngine,Board board) {
	List<Player> players = new ArrayList<Player>();

	Player greenPlayer = new HumanPlayer(((LudoScreen) screen), ruleEngine);
		greenPlayer.setColor(COLOR.GREEN);
		greenPlayer.setTurn(true);
		greenPlayer.setPieces(board.getPiecesMap().get(greenPlayer.getColor()));
		players.add(greenPlayer);

//		Player yellowPlayer = new HumanPlayer(((LudoScreen) screen), ruleEngine);
//		yellowPlayer.setColor(COLOR.YELLOW);
//		yellowPlayer.setTurn(false);
//		yellowPlayer.setStartIndex(Board.DIMENSION * 2 + 1);
//		yellowPlayer.setPieces(board.getPiecesMap().get(yellowPlayer.getColor()));
//		players.add(yellowPlayer);
//
		Player redPlayer = new HumanPlayer(((LudoScreen) screen), ruleEngine);
		redPlayer.setColor(COLOR.RED);
		redPlayer.setTurn(false);
		redPlayer.setStartIndex(Board.DIMENSION * 4 + 2);
		redPlayer.setPieces(board.getPiecesMap().get(redPlayer.getColor()));
		players.add(redPlayer);
//
//		Player bluePlayer = new HumanPlayer(((LudoScreen) screen), ruleEngine);
//		bluePlayer.setColor(COLOR.BLUE);
//		bluePlayer.setTurn(false);
//		bluePlayer.setStartIndex(Board.DIMENSION * 6 + 3);
//		bluePlayer.setPieces(board.getPiecesMap().get(bluePlayer.getColor()));
//		players.add(bluePlayer);
		return players;
	}

	public static  List<Player> createRobotPlayers(Screen screen,RuleEngine ruleEngine,Board board) {
		List<Player> players = new ArrayList<Player>();

		Player greenPlayer = new HumanPlayer(((LudoScreen) screen), ruleEngine);
		greenPlayer.setColor(COLOR.GREEN);
		greenPlayer.setPieces(board.getPiecesMap().get(greenPlayer.getColor()));
		players.add(greenPlayer);

		Player yellowPlayer = new ComputerPlayer(((LudoScreen) screen), ruleEngine);
		yellowPlayer.setColor(COLOR.YELLOW);
		yellowPlayer.setStartIndex(Board.DIMENSION * 2 + 1);
		yellowPlayer.setPieces(board.getPiecesMap().get(yellowPlayer.getColor()));
		players.add(yellowPlayer);

		Player redPlayer = new ComputerPlayer(((LudoScreen) screen), ruleEngine);
		redPlayer.setColor(COLOR.RED);
		redPlayer.setStartIndex(Board.DIMENSION * 4 + 2);
		redPlayer.setPieces(board.getPiecesMap().get(redPlayer.getColor()));
		players.add(redPlayer);

		Player bluePlayer = new ComputerPlayer(((LudoScreen) screen), ruleEngine);
		bluePlayer.setColor(COLOR.BLUE);
		bluePlayer.setStartIndex(Board.DIMENSION * 6 + 3);
		bluePlayer.setPieces(board.getPiecesMap().get(bluePlayer.getColor()));
		players.add(bluePlayer);
		return players;
	}

	public static List<Player> createMinMaxPlayers(Screen screen,RuleEngine ruleEngine,Board board) {
		List<Player> players = new ArrayList<Player>();
 
		Player greenPlayer = new ANNPlayer(((LudoScreen) screen), ruleEngine);
		greenPlayer.setColor(COLOR.GREEN);
		greenPlayer.setPieces(board.getPiecesMap().get(greenPlayer.getColor()));
		players.add(greenPlayer);

		Player yellowPlayer = new MinMaxPlayer(((LudoScreen) screen), ruleEngine);
		 yellowPlayer.setColor(COLOR.YELLOW);
		 yellowPlayer.setStartIndex(Board.DIMENSION * 2 + 1);
		 yellowPlayer.setPieces(board.getPiecesMap().get(yellowPlayer.getColor()));
		 players.add(yellowPlayer);
		
		 Player redPlayer = new MinMaxPlayer(((LudoScreen) screen), ruleEngine);
		 redPlayer.setColor(COLOR.RED);
		 redPlayer.setStartIndex(Board.DIMENSION * 4 + 2);
		 redPlayer.setPieces(board.getPiecesMap().get(redPlayer.getColor()));
		 players.add(redPlayer);
		
		 Player bluePlayer = new MinMaxPlayer(((LudoScreen) screen), ruleEngine);
		 bluePlayer.setColor(COLOR.BLUE);
		 bluePlayer.setStartIndex(Board.DIMENSION * 6 + 3);
		 bluePlayer.setPieces(board.getPiecesMap().get(bluePlayer.getColor()));
		 players.add(bluePlayer);
		return players;
	}
}
