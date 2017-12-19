package com.tgt.ludo.gamestate;

import com.badlogic.gdx.Screen;
import com.tgt.ludo.board.Board;
import com.tgt.ludo.board.Piece;
import com.tgt.ludo.board.Square;
import com.tgt.ludo.player.Player;
import com.tgt.ludo.player.action.Kill;
import com.tgt.ludo.player.action.Move;
import com.tgt.ludo.ui.LudoScreen;

/***
 * Main class controlling a single game session
 * 
 * @author robin
 *
 */
public class AdvancedLudoGameStateController extends LudoGameStateController {

	
	public AdvancedLudoGameStateController(Screen screen) {
		super(screen);
	}

	protected void play(Player player, int playerIndex) {
		super.play(player, playerIndex);

		// check game state after play

	}

	/***
	 * 
	 */
	@Override
	protected boolean checkGameState() {
		
		if (killCheck())
			return true;
		
		if (jailCheck())
			return true;

		if (homeCheck())
			return true;

		return false;
	}

	private boolean killCheck() {

		Kill kill = ruleEngine.getKills();
		if (kill.getKilledPiece() != null && kill.getKilledPiece().size() > 0) {
			Piece killedPiece = kill.getKilledPiece().get(0);
			killedPiece.setKilled(true);
			sendToRest(killedPiece);
			//System.out.println("Another turn: " + player.getColor());
			getPlayer(kill.getKillerPiece().getColor()).setTurn(true);
			return true;
		}
		return false;
	}

	private void sendToRest(Piece piece) {
		movePieceOutsideTrack(new Move(piece));
	}

	private void sendToHome(Piece piece) {
		movePieceOutsideTrack(new Move(piece));
		// give chance back to player
		getPlayer(piece.getColor()).setTurn(true);
	}

	private boolean jailCheck() {
		Piece jailedPiece = ruleEngine.getPieceOnJail();

		if (jailedPiece != null) {
			jailedPiece.setJailed(true);
			sendToRest(jailedPiece);
			return true;
		}
		return false;
	}

	private boolean homeCheck() {
		Piece homePiece = ruleEngine.getPieceOnHomeSquare();
		if (homePiece != null) {
			homePiece.setToHome(true);
			sendToHome(homePiece);
			return true;
		}
		return false;
	}

	private Player getPlayer(Board.COLOR color) {
		for (Player player : getPlayers()) {
			if (player.getColor().equals(color)) {
				return player;
			}
		}
		return null;
	}

	protected void movePieceOutsideTrack(Move move) {
		((LudoScreen) screen).getBoardRenderer().setPieceMovingOutSideTrack(move);
		move.getPiece().getSittingSuare().getPieces().remove(move.getPiece());
		//sittingSquareIndex = move.getPiece().getSittingSuare().getIndex();
		move.getPiece().setShake(false);
		shakeDice(false);
	}
}
