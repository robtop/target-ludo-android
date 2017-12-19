package com.tgt.ludo.gamestate;

import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.Screen;
import com.tgt.ludo.board.Board;
import com.tgt.ludo.board.Dice;
import com.tgt.ludo.board.Piece;
import com.tgt.ludo.gamestate.util.GameStateUtil;
import com.tgt.ludo.player.Player;
import com.tgt.ludo.player.action.Move;
import com.tgt.ludo.rules.BasicRuleEngine;
import com.tgt.ludo.rules.RuleEngine;
import com.tgt.ludo.ui.LudoScreen;

/***
 * Main class controlling a single game session
 * 
 * @author robin
 *
 */
public class LudoGameStateController implements GameStateController {
	protected Board board;

	// needed by human players to get inputs
	protected LudoScreen screen;
	protected List<Player> players;

	// TODO: used in games supporting remote players
	private UUID gameID;
	private GAME_STATE gameState;
	private Player winner;
	protected RuleEngine ruleEngine;
	protected Player player;

	public LudoGameStateController(Screen screen) {

		gameID = UUID.randomUUID();
		gameState = GAME_STATE.WAITING;
		board = new Board();
		board.setup();
		ruleEngine = new BasicRuleEngine(board);
		this.screen = (LudoScreen) screen;
		// createPlayers();
		// createRobotPlayers();
		//players = GameStateUtil.createPlayers(screen, ruleEngine, board);
		players = GameStateUtil.createRobotPlayers(screen, ruleEngine, board);
		gameState = GAME_STATE.PROGRESS;
	}

	//protected Move move;

	/***
	 * Main game loop to update the backend and allow play
	 */
	public void update() {
		// wait for piece motion animation to complete
		if (!((LudoScreen) screen).getBoardRenderer().isAnimationComplete()) {
			return;
		}

		// check for any events after animation/piece moves
		if (checkGameState())
			return;

		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);

			if (player.isTurn()) {
				this.player = player;
				play(player, i);
				break;
			}
		}
	}

	protected boolean checkGameState() {
		return false;
	}

	protected void play(Player player, int playerIndex) {
		 Move move = player.play();
		if (move != null) {

			if (move.isSkipTurn()) {
				giveTurnToNext(player, playerIndex);
				return;
			}
			if (move.isIncomplete()) {
				player.setSelectDice(false);
				player.setDiceRolled(true);
			} else {
				giveTurnToNext(player, playerIndex);
				setPiecesShake(player, false);

			}

			// do the actual move in the board backend
			movePieceInTrack(player, move);

			// check game state for win etc after the animation completes
		}
	}

	private void setPiecesShake(Player player, boolean shake) {
		for (Piece piece : player.getPieces()) {
			piece.setShake(shake);
		}
	}

	protected void movePieceInTrack(Player player, Move move) {
		if (move == null || move.getPiece() == null) {
			return;
		}
		((LudoScreen) screen).getBoardRenderer().setPieceMovingInTrack(player, move);
		//move.getPiece().getSittingSuare().getPieces().remove(move.getPiece());
		move.getPiece().setShake(false);
		shakeDice(false);
	}

	private void giveTurnToNext(Player currentPlayer, int i) {
		currentPlayer.setTurn(false);
		Player selectedPlayer;
		if (i + 1 < players.size()) {
			selectedPlayer = players.get(i + 1);

		} else {
			selectedPlayer = players.get(0);
		}
		
		selectedPlayer.setTurn(true);

		((LudoScreen) screen).getBoardRenderer().resetRenderer();
		((LudoScreen) screen).getBoardRenderer().setSelectedPlayer(selectedPlayer);
	}

	protected void shakeDice(boolean shake) {
		List<Dice> diceList = screen.getBoardRenderer().getDiceList();
		for (Dice dice : diceList) {
			dice.setShake(shake);
		}
	}

	public Board getBoard() {
		return board;
	}

	public GAME_STATE getGameState() {
		return gameState;
	}

	public List<Player> getPlayers() {
		return players;
	}

}
