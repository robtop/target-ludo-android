package com.tgt.ludo.util;

import java.io.BufferedReader;
import java.io.FileReader;

import com.tgt.ludo.board.Board;
import com.tgt.ludo.board.Board.COLOR;
import com.tgt.ludo.board.Square;
import com.tgt.ludo.player.action.Move;



public class LudoUtil {

	public static int calulateDestIndex(Move move) {

		Board.COLOR color = move.getPiece().getColor();

		int sittingIndex = move.getPiece().getSittingSuare().getIndex();
		int dest =   sittingIndex + move.getSquares();
		// System.out.println("sittingIndex: " + sittingIndex);
		// if (move.getSquares() + sittingIndex == Board.TOTAL_NUM_SQUARES - 1)
		// {
		// // move to home square
		// } else
		if (dest >= Board.TOTAL_NUM_SQUARES) {
			return (dest) % Board.TOTAL_NUM_SQUARES;
		}
		// System.out.println("calulateDestIndex: " + dest);
		return dest;
	}
	
	public static int calulateDestIndexInHome(Move move) {

		Board.COLOR color = move.getPiece().getColor();

		int sittingIndex = move.getPiece().getSittingSuare().getIndex();
		int dest =   sittingIndex + move.getSquares();
		return dest;
	}

	public static int getStartIndex(COLOR color) {
		int startIndex = 0;
		switch (color) {
		case GREEN:
			startIndex = Board.startIndexes.get(0);
			break;
		case YELLOW:
			startIndex = Board.startIndexes.get(1);
			break;
		case RED:
			startIndex = Board.startIndexes.get(2);
			break;
		case BLUE:
			startIndex = Board.startIndexes.get(3);
			break;
		}
		return startIndex;
	}

	/**
	 * Calculates the next square index. If track end is reached, we get back zero the start of the track.
	 * If the piece has finished his track we get back zero the start of the home squares (the isHomeSq property is set)
	 * 
	 * @param pieceMove
	 * @param moveCount - the count of steps already taken by the piece from the sitting square position
	 * @return
	 */
	public static int calulateNextIndex(Move pieceMove, int moveCount) {

		// shouldnt come here
		if (pieceMove.getPiece().isHomeSq()) {
			return -99;
		}
		int newMoveTempIndex = 0;

		int currentIndex = (pieceMove.getPiece().getSittingSuare().getIndex() + moveCount) % Board.TOTAL_NUM_SQUARES;

		newMoveTempIndex = currentIndex;
		if (pieceMove.getPiece().getMoveCount() ==  Board.TOTAL_NUM_SQUARES) {
			// move to home square
			pieceMove.getPiece().setHomeSq(true);
			newMoveTempIndex = 0;
		} else if (newMoveTempIndex + 1 >= Board.TOTAL_NUM_SQUARES) {
			newMoveTempIndex = 0;
		} else {
			newMoveTempIndex += 1;
		}
		// System.out.println("calulateNextIndex: " + moveTempIndex);

		pieceMove.getPiece().setMoveCount(pieceMove.getPiece().getMoveCount() + 1);
		// System.out.println(
		// "total moves: " +pieceMove.getPiece().getMoveCount());
		return newMoveTempIndex;
	}
	
	public static int calulateNextHomeSqIndex(Move pieceMove, int moveCount) {

		int newMoveTempIndex = 0;

		int currentIndex = (pieceMove.getPiece().getSittingSuare().getIndex() + moveCount) % 68;

		newMoveTempIndex = currentIndex;
		if ((pieceMove.getPiece().getMoveCount() + 1) % Board.TOTAL_NUM_SQUARES == 0) {
			// move to home square
			pieceMove.getPiece().setHomeSq(true);
			newMoveTempIndex = 0;
		} else if (newMoveTempIndex + 1 >= Board.TOTAL_NUM_SQUARES) {
			newMoveTempIndex = 0;
		} else {
			newMoveTempIndex += 1;
		}
		// System.out.println("calulateNextIndex: " + moveTempIndex);

		//pieceMove.getPiece().setMoveCount(pieceMove.getPiece().getMoveCount() + 1);
		// System.out.println(
		// "total moves: " +pieceMove.getPiece().getMoveCount());
		return newMoveTempIndex;
	}

	public static Square getFreeRestSquare(Board.COLOR color, Board board) {
		for (Square square : board.getRestSquaresMap().get(color)) {
			if (square.getPieces().isEmpty()) {
				return square;
			}
		}
		// TODO: bug returns null sometimes - shouldnt
		return board.getRestSquaresMap().get(color).get(0);
		// return null;
	}
}
