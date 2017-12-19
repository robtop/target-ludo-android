package com.tgt.ludo.net;

import com.tgt.ludo.player.Player;
import com.tgt.ludo.player.action.Move;

public interface LudoService {

	//TODO: connect to remote server
	
	//send the player move, dice roll
	public boolean sendGameStatetoServer();
	
	/***
	 * wait for all player to get current game state - 
	 * no support for offline play in case one player gets out of network - remove them from the game
	 * @return
	 */
	public boolean remotePlayersInsync();
	
	/**
	 * Let remote player play
	 * @param player
	 * @return
	 */
	public Move play(Player player);
	
	public void rollDice(Player player);
	
}
