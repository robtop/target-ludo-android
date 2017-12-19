package com.tgt.ludo.rules;

import java.util.ArrayList;
import java.util.List;

import com.tgt.ludo.board.Board;
import com.tgt.ludo.board.Piece;
import com.tgt.ludo.board.Square;
import com.tgt.ludo.player.Player;
import com.tgt.ludo.player.action.Kill;
import com.tgt.ludo.player.action.Move;
import com.tgt.ludo.util.LudoUtil;

/**
 * Engine to enforce rules of ludo.
 * 
 * @author robin
 *
 */
public class BasicRuleEngine implements RuleEngine {

	private Board board;

	@Override
	public boolean validMove(Piece piece, int diceVal) {
		if (piece.isMainHome()) {
			return false;
		}
		if (piece.isRest()) {
			if (diceVal == 6)
				return true;
		} else if (piece.isHomeSq()) {
			if (piece.getSittingSuare().getIndex() + diceVal <= Board.DIMENSION) {
				return true;
			}else {
				return false;
			}

		} else
		{
			return true;
		}

		return false;
	}

	int prev = 0;

	public int getSingleDiceRoll() {
		// get range 1 to 6
		int value = (int) Math.floor((Math.random() * 6)) + 1;
		// if (prev != 6)
		// value = 6;
		prev = value;
		// System.out.println("Dice Roll: " + value);
		return value;

	}

	@Override
	public List<Move> getValidMoves(Player player, int diceVal) {
		List<Move> moves = new ArrayList<Move>();
		for (Piece piece : player.getPieces()) {
			if (piece.isMainHome()) {
				continue;
			}
			if (piece.isRest() && diceVal == 6) {
				Move move = new Move(piece);
				move.setSquares(0);
				moves.add(move);
			}

			if (validMove(piece, diceVal)) {
				Move move = new Move(piece);
				move.setSquares(diceVal);
				moves.add(move);
			}
		}

		return moves;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public BasicRuleEngine(Board board) {
		this.board = board;
	}

	@Override
	public Kill getKills() {
		List<Piece> killePieces = new ArrayList<Piece>();
		Kill kill = new Kill();
		for (Square square : board.getSquares()) {
			if (square.getPieces().size() > 1) {
				// checking two is enough, otherwise it would be a block
				if (!square.getPieces().get(0).getColor().equals(square.getPieces().get(1).getColor())) {
					// older piece gets killed
					// if a variation the add more than ones
					killePieces.add(square.getPieces().get(0));
					kill.setKilledPiece(killePieces);
					kill.setKillerPiece(square.getPieces().get(1));
				}
			}
		}

		return kill;
	}

	@Override
	public Piece getPieceOnHomeSquare() {
		Piece homePiece = null;
		if (!board.getSquares().get(Board.HOME_INDEX).getPieces().isEmpty()) {
			homePiece = board.getSquares().get(Board.HOME_INDEX).getPieces().get(0);
		}
		return homePiece;
	}

	@Override
	public Piece getPieceOnJail() {
		for (Integer i : board.jailIndexes) {
			if (!board.getSquares().get(i).getPieces().isEmpty()) {
				Piece jailedPiece = board.getSquares().get(i).getPieces().get(0);
				return jailedPiece;
			}
		}
		return null;
	}

	@Override
	public boolean goToJail(Move move) {
		int dest = move.getPiece().getSittingSuare().getIndex() + move.getSquares();
		if (Board.jailIndexes.contains(dest)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean goToHomeSquare(Move move) {
		int dest = move.getPiece().getSittingSuare().getIndex() + move.getSquares();
		if (Board.HOME_INDEX == dest) {
			return true;
		}
		return false;
	}

	@Override
	public boolean makeAkill(Move move) {
		int dest = LudoUtil.calulateDestIndex(move);
		if (!board.getSquares().get(dest).getPieces().isEmpty()) {
			for (Piece piece : board.getSquares().get(dest).getPieces()) {
				if (piece.getColor() != move.getPiece().getColor()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean closeToKill(Move move) {
		int dest = LudoUtil.calulateDestIndex(move);
		for (int i = dest; i < 6; i++) {
			if (!board.getSquares().get(i).getPieces().isEmpty()) {
				for (Piece piece : board.getSquares().get(i).getPieces()) {
					if (piece.getColor() != move.getPiece().getColor()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean jumpJail(Move move) {
		int currentIndex = move.getPiece().getSittingSuare().getIndex();
		int dest = LudoUtil.calulateDestIndex(move);
		// wont work around the track corner coz dest < currentIndex
		for (int i = currentIndex; i < dest; i++) {
			if (board.jailIndexes.contains(i)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean escapeKill(Move move) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reachHome(Move move) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int dicePerGame() {
		return 1;
	}
}
