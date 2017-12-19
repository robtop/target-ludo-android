package com.tgt.ludo.gamestate;

import java.util.List;
import com.tgt.ludo.board.Board;
import com.tgt.ludo.player.Player;

/***
 * Interface of the main game state controller. This update loop updates and maintains the game play.
 * 
 * @author robin
 *
 */
public interface GameStateController {

	public static enum GAME_STATE {
		WAITING, PROGRESS, COMPLETE
	}

	
	Board getBoard();

	List<Player> getPlayers();

	void update();

}
