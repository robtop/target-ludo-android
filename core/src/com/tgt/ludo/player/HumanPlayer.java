package com.tgt.ludo.player;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.tgt.ludo.board.Dice;
import com.tgt.ludo.board.Piece;
import com.tgt.ludo.player.action.Move;
import com.tgt.ludo.rules.RuleEngine;
import com.tgt.ludo.ui.BoardRenderer;
import com.tgt.ludo.ui.LudoScreen;

public class HumanPlayer extends Player {

	// human player needs eyes to capture inputs from the screen
	// OrthographicCamera guiCam;
	PerspectiveCamera cam3D;
	protected Vector3 touchPoint;
	Ray pickRay;
	// need screen details to capture inputs and get location of pieces
	LudoScreen screen;
	List<Dice> diceList;

	public HumanPlayer(LudoScreen screen, RuleEngine ruleEngine) {
		super(ruleEngine);
		this.screen = screen;
		// this.guiCam = screen.getGuiCam();
		this.cam3D = screen.getCam();
		touchPoint = new Vector3();

	}

	/***
	 * Main game loop for human player
	 * 
	 */
	@Override
	public Move play() {
		diceList = screen.getBoardRenderer().getDiceList();

		// player has not yet rolled the dice, so roll and retrun
		if (!diceRolled) {
			rollDice();
			return null;
		}
     
		List<Move> moves = new ArrayList<Move>();
		for (Dice dice : diceList) {
			moves.addAll(ruleEngine.getValidMoves(this, dice.getDiceValue()));
		}

		if (moves.isEmpty()) {
			diceRolled = false;
			// skip turn
			return new Move(true);
		}
		
	
		// player has rolled dice and selected a piece - select which Die value
		// to apply - in case of a 6 followed by another roll
		if (selectDice) {
			return getDiceMove();
		}

		// select the piece to play and return the move to gameState
		return selectPiece();
	}


	private Move selectPiece() {
		// get all valid moves the player can play
		List<Move> moves = new ArrayList<Move>();
		for (Dice dice : diceList) {
			moves.addAll(ruleEngine.getValidMoves(this, dice.getDiceValue()));
		}

		if (moves.isEmpty()) {
			diceRolled = false;
			// skip turn
			return new Move(true);
		}

		// shake all pieces that can move
		for (Move move : moves) {
			move.getPiece().setShake(true);
		}
		// get user input from the screen
		return capturePieceInput();
	}

	/**
	 * Which piece does the human touch?
	 * 
	 * @return
	 */
	private Move capturePieceInput() {
		// dice is rolled in previous play loop - now select the piece to move
		if (Gdx.input.justTouched()) {
			touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Piece piece = getSelectedPiece();
			if (piece == null) {
				return null;
			}

			// assuming single dice
			// check if valid move and move
			//System.out.println("Touched: " + piece);

			if (diceList.size() == 1 ) {

				if (ruleEngine.validMove(piece, diceList.get(0).getDiceValue())) {
					
					//TODO: bug here
					diceRolled = false;
					return new Move(piece, diceList.get(0).getDiceValue());
				}
			} // TODO: 2 variation
			else {
				selectDice = true;
				
				for (Piece pieceShake : pieces) {
					pieceShake.setShake(false);
				}
				selectedPiece = piece;
				selectedPiece.setShake(true);
			}

		}
		return null;
	}

	private void shakeDice(boolean shake) {
		List<Dice> diceList = screen.getBoardRenderer().getDiceList();
		for (Dice dice : diceList) {
			dice.setShake(shake);
		}
	}

	/**
	 * Fire a ray at touched point (by human) and see which piece it intersects
	 */
	public Piece getSelectedPiece() {
		// check if touched
		pickRay = cam3D.getPickRay(touchPoint.x, touchPoint.y, 0, 0, Gdx.app.getGraphics().getWidth(),
				Gdx.app.getGraphics().getHeight());
		Vector3 intersection = new Vector3();
		for (Piece piece : pieces) {
			if(!piece.isShake()){
				continue;
			}
			Vector3 tran = new Vector3();
			screen.getBoardRenderer().getPieceInstMap().get(piece).transform.getTranslation(tran);
			if (Intersector.intersectRaySphere(pickRay, tran, BoardRenderer.SQUARE_LENGTH, intersection)) {
				return piece;
			}

		}
		return null;
	}

	protected Move getDiceMove() {
		shakeDice(true);
		Dice dice = captureDiceInput();
		if (dice == null) {
			return null;
		}
		if(!ruleEngine.validMove(selectedPiece, dice.getDiceValue())){
			return null;
		}
		diceList.remove(dice);

		if (diceList.isEmpty()) {
			selectDice = false;
			selectedPiece.setShake(false);
			return new Move(selectedPiece, dice.getDiceValue());
		}
		Move move = new Move(selectedPiece, dice.getDiceValue());
		move.setIncomplete(true);
		return move;
	}

	public void rollDice() {
		List<Integer> diceValList = captureDiceRollInput();
		if (!(diceValList == null)) {

			diceRolled = true;
		}
	}

	/**
	 * Multiple dice, which does the human touch?
	 * 
	 * @return
	 */
	protected Dice captureDiceInput() {
		List<Dice> diceList = screen.getBoardRenderer().getDiceList();
		// only last dice eligible to be touched - others should be six -

		if (Gdx.input.justTouched()) {
			for (Dice dice : diceList) {
				touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				pickRay = cam3D.getPickRay(touchPoint.x, touchPoint.y, 0, 0, Gdx.app.getGraphics().getWidth(),
						Gdx.app.getGraphics().getHeight());
				Vector3 intersection = new Vector3();
				Vector3 tran = new Vector3();
				dice.getDiceInstance().transform.getTranslation(tran);
				if (Intersector.intersectRaySphere(pickRay, tran, BoardRenderer.SQUARE_LENGTH, intersection)) {
					return dice;
				}
			}
		}
		return null;
	}

	/**
	 *  player has rolled dice and selected a piece - capture the user input of the die value to apply to the piece
	 *
	 * @return
	 */
	protected List<Integer> captureDiceRollInput() {

		// only last dice eligible to be touched - others should be six -
		// //TODO: check variation with two dice
		Dice dice = diceList.get(diceList.size() - 1);
		if (Gdx.input.justTouched()) {
			touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			pickRay = cam3D.getPickRay(touchPoint.x, touchPoint.y, 0, 0, Gdx.app.getGraphics().getWidth(),
					Gdx.app.getGraphics().getHeight());
			Vector3 intersection = new Vector3();
			Vector3 tran = new Vector3();
			dice.getDiceInstance().transform.getTranslation(tran);
			if (Intersector.intersectRaySphere(pickRay, tran, BoardRenderer.SQUARE_LENGTH, intersection)) {
				return super.rollDice(dice, screen.getBoardRenderer());
			}
		}
		return null;
	}

}
