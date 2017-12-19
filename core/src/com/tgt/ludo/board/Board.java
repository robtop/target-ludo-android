package com.tgt.ludo.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tgt.ludo.player.action.Move;

/**
 * The main backend class representing a games board data
 * 
 * @author robin
 *
 */
public class Board {

	// length of one arm of the board
	public static final int DIMENSION = 8;
	// start location of a player wrt dimension
	public static final int START = 0;
	// index from start point of each color
	// public static final int JAIL_INDEX = 12;
	// special home square
	public static final int HOME_INDEX = 60;

	public static enum COLOR {
		GREEN, YELLOW, RED, BLUE
	}

	public static final  List<Integer> jailIndexes =  new ArrayList<Integer>(Arrays.asList(13, 30, 47, 64)); //new ArrayList<Integer>(Arrays.asList(6,9,11,13));

	public static final List<Integer> startIndexes = new ArrayList<Integer>(
			Arrays.asList(0, DIMENSION * 2 + 1, DIMENSION * 4 + 2, DIMENSION * 6 + 3));

	//main track
	public static final int TOTAL_NUM_SQUARES = DIMENSION * 4 * 2 + 4;
	
	private List<Square> squares;

	private Map<COLOR, List<Square>> homeSquaresMap;

	private Map<COLOR, List<Square>> restSquaresMap;

	private Map<COLOR, List<Piece>> piecesMap;
	private int players = 4;
	Map<COLOR,Square> homeMap = new HashMap<Board.COLOR, Square>(); 
	
	public void setup(int players) {
		this.players = players;
		setup();
	}

	public void setup() {
		createSquares();
		createHomeSquares();
		createRestSquares();
		createPieces();
		createHomeMap();
	}

	private void createSquares() {
		
		int numHomeSqrsPerColor = DIMENSION - 1;
		squares = new ArrayList<Square>();
		for (int i = 0; i < TOTAL_NUM_SQUARES; i++) {
			Square sq = new Square();
			sq.setIndex(i);

			if (jailIndexes.contains(i)) {
				sq.setJail(true);
			} else if (startIndexes.contains(i)) {
				sq.setStartSquare(true);
			} else

			if (i == HOME_INDEX) {
				sq.setHome(true);
			}
			squares.add(sq);
		}

		// squares.get(0).setColor(COLOR.GREEN);
		// squares.get(DIMENSION * 2 + 2).setColor(COLOR.YELLOW);
		// squares.get(DIMENSION * 4 + 2).setColor(COLOR.RED);
		// squares.get(DIMENSION * 6 + 2).setColor(COLOR.BLUE);
	}

	private void createHomeSquares() {
		homeSquaresMap = new HashMap<Board.COLOR, List<Square>>();
		homeSquaresMap.put(COLOR.GREEN, createHomeSquareList(COLOR.GREEN));

		homeSquaresMap.put(COLOR.YELLOW, createHomeSquareList(COLOR.YELLOW));

		homeSquaresMap.put(COLOR.RED, createHomeSquareList(COLOR.RED));

		homeSquaresMap.put(COLOR.BLUE, createHomeSquareList(COLOR.BLUE));

	}

	
	
	private void createRestSquares() {
		restSquaresMap = new HashMap<Board.COLOR, List<Square>>();
		restSquaresMap.put(COLOR.GREEN, createRestSquareList(COLOR.GREEN));
		restSquaresMap.put(COLOR.YELLOW, createRestSquareList(COLOR.YELLOW));
		restSquaresMap.put(COLOR.RED, createRestSquareList(COLOR.RED));
		restSquaresMap.put(COLOR.BLUE, createRestSquareList(COLOR.BLUE));
	}

	private List<Square> createHomeSquareList(Board.COLOR color) {
		List<Square> list = new ArrayList<Square>();
		for (int i = 0; i < DIMENSION - 1; i++) {
			Square sq = new Square();
			sq.setHome(true);
			sq.setColor(color);
			list.add(sq);
		}
		return list;
	}

	private List<Square> createRestSquareList(Board.COLOR color) {
		List<Square> list = new ArrayList<Square>();
		for (int i = 0; i < 4; i++) {
			Square sq = new Square();
			sq.setRestSquare(true);
			sq.setHome(true);
			sq.setColor(color);
			list.add(sq);
		}
		return list;
	}

	private void createPieces() {
		piecesMap = new HashMap<Board.COLOR, List<Piece>>();
		piecesMap.put(COLOR.GREEN, createPiecesList(COLOR.GREEN));
		if (players > 2) {
			piecesMap.put(COLOR.YELLOW, createPiecesList(COLOR.YELLOW));
		}
		piecesMap.put(COLOR.RED, createPiecesList(COLOR.RED));
		if (players > 3) {
			piecesMap.put(COLOR.BLUE, createPiecesList(COLOR.BLUE));
		}
	}

	private List<Piece> createPiecesList(Board.COLOR color) {
		List<Piece> list = new ArrayList<Piece>();
		for (int i = 0; i < 4; i++) {
			Piece piece = new Piece();
			piece.setColor(color);
			piece.setRest(true);
			list.add(piece);
			placePieceInRestSq(piece, color);
		}
		return list;
	}

	/**
	 * Only called on creation
	 * 
	 * @param piece
	 * @param color
	 */
	private void placePieceInRestSq(Piece piece, COLOR color) {
		// find empty square and place
		for (Square sq : restSquaresMap.get(color)) {
			if (sq.getPieces() == null || sq.getPieces().isEmpty()) {
				List<Piece> list = new ArrayList<Piece>();
				list.add(piece);
				piece.setSittingSuare(sq);
				sq.setPieces(list);
				return;
			}
		}
	}

	private void createHomeMap(){
		homeMap.put(COLOR.GREEN, new Square());
		homeMap.put(COLOR.YELLOW, new Square());
		homeMap.put(COLOR.RED, new Square());
		homeMap.put(COLOR.BLUE, new Square());
	}
	public List<Square> getSquares() {
		return squares;
	}

	public void setSquares(List<Square> squares) {
		this.squares = squares;
	}

	public Map<COLOR, Square> getHomeMap() {
		return homeMap;
	}

	public void setHomeMap(Map<COLOR, Square> homeMap) {
		this.homeMap = homeMap;
	}

	public Map<COLOR, List<Square>> getHomeSquaresMap() {
		return homeSquaresMap;
	}

	public Map<COLOR, List<Square>> getRestSquaresMap() {
		return restSquaresMap;
	}

	public Map<COLOR, List<Piece>> getPiecesMap() {
		return piecesMap;
	}

	public void setHomeSquaresMap(Map<COLOR, List<Square>> homeSquaresMap) {
		this.homeSquaresMap = homeSquaresMap;
	}

}