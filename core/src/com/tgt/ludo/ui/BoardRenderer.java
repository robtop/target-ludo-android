package com.tgt.ludo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3;
import com.tgt.ludo.board.Board;
import com.tgt.ludo.board.Dice;
import com.tgt.ludo.board.Piece;
import com.tgt.ludo.board.Square;
import com.tgt.ludo.player.Player;
import com.tgt.ludo.player.action.Move;
import com.tgt.ludo.util.LudoUtil;

public class BoardRenderer extends StaticBoardRenderer {

	protected boolean animationComplete = true;
	protected Move pieceMove;
	protected int moveFinalIndex;
	private int moveTempIndex;
	private int moveCount;
	private int moveCountHomeSq;

	// flags for animation
	protected boolean restMovedToStart;
	private boolean movedToRest;
	private boolean homeSqMovedToHome;

	private Square moveToRestSq;
	private Square moveToHome;
	private boolean killMovedToRest;

	protected static final int MOVE_SPEED = 10;
	// protected ModelInstance pieceInstance;
	private List<Dice> diceList;
	protected Model diceModel;
	protected Player selectedPlayer;

	public BoardRenderer(Board board, RenderContext renderContext, PerspectiveCamera cam, Environment environment) {
		super(board, renderContext, cam, environment);
		assetsManager.load("meshes/dice1.g3dj", Model.class);
		assetsManager.load("meshes/dice2.g3dj", Model.class);
		assetsManager.load("meshes/dice3.g3dj", Model.class);
		assetsManager.load("meshes/dice4.g3dj", Model.class);
		assetsManager.load("meshes/dice5.g3dj", Model.class);
		assetsManager.load("meshes/dice6.g3dj", Model.class);

		assetsManager.finishLoading();

		diceList = new ArrayList<Dice>();
		Dice newDice = createDiceInstance();
		newDice.setShake(true);
		diceList.add(newDice);
	}

	/**
	 * Clear all flags and temp variables when a new player starts
	 */
	public void resetRenderer() {

		animationComplete = true;
		if (pieceMove != null) {
			pieceMove.setPiece(null);
			pieceMove.setSquares(-99);
		}
		pieceMove = null;
		moveFinalIndex = -999;
		moveTempIndex = -999;
		moveCount = -999;
		moveCountHomeSq = 0;
		restMovedToStart = false;
		movedToRest = false;
		homeSqMovedToHome = false;

		moveToRestSq = null;
		moveToHome = null;

		diceList = new ArrayList<>();
		for (int d = 0; d < selectedPlayer.getRuleEngine().dicePerGame(); d++) {
			Dice newDice = createDiceInstance();
			newDice.setShake(true);
			diceList.add(newDice);
		}

	}

	public void render(float delta) {
		super.render(delta);

		renderContext.begin();
		modelBatch.begin(cam);
		if (!animationComplete) {
			if (pieceMove.getPiece().isRest()) {
				renderMovingFromRestPiece(delta);
			} else if (pieceMove.getPiece().isJailed() || pieceMove.getPiece().isKilled()) {
				renderMovingToRestSquare(delta);
			} else if (pieceMove.getPiece().isToHome()) {
				renderMovingToHome(delta);
			} else if (pieceMove.getPiece().isHomeSq()) {
				renderMovingPieceInHomeSquares(delta);
			} else {
				renderMovingPiece(delta);
			}
		}

		renderDice(delta);
		modelBatch.end();
		renderContext.end();
	}

	public void renderMovingPiece(float delta) {
		ModelInstance pieceInstance = pieceInstMap.get(pieceMove.getPiece());
		if (moveCount == pieceMove.getSquares() - 1) {
			Vector3 trans = new Vector3();
			// TODO: bug moveFinalIndex getting the old piece value if same
			// color
			squareInstMap.get(board.getSquares().get(moveFinalIndex)).transform.getTranslation(trans);
			// set the destination squares translation to the piece
			pieceInstance.transform.setTranslation(trans);
			Square finalSquare = board.getSquares().get(LudoUtil.calulateDestIndex(pieceMove));
			finalSquare.getPieces().add(pieceMove.getPiece());
			pieceMove.getPiece().getSittingSuare().getPieces().remove(pieceMove.getPiece());
			pieceMove.getPiece().setSittingSuare(finalSquare);
			// System.out.println(pieceMove.getPiece() + ":
			// "+finalSquare.getIndex() + " Completed");
			animationComplete = true;
			return;
		}
		// check if it reached its home square or home etc

		Vector3 currentTranslation = new Vector3();
		Vector3 finalTranslation = new Vector3();

		pieceInstance.transform.getTranslation(currentTranslation);

		squareInstMap.get(board.getSquares().get(moveTempIndex)).transform.getTranslation(finalTranslation);

		Vector3 diff = finalTranslation.sub(currentTranslation);
		modelBatch.render(pieceInstance, environment);
		if (diff.len() < .1f) {
			moveTempIndex = LudoUtil.calulateNextIndex(pieceMove, moveCount);
			//System.out.println(pieceMove.getPiece() + ": " + pieceMove.getPiece().getMoveCount());
			moveCount++;
			moveCountHomeSq++;
		} else {

			pieceInstance.transform.translate(diff.scl(delta * MOVE_SPEED));
		}

	}

	public void renderMovingPieceInHomeSquares(float delta) {
		ModelInstance pieceInstance = pieceInstMap.get(pieceMove.getPiece());
		
		if (moveCount == pieceMove.getSquares() - 1) {
			//simplify game for testing
			moveToHome = board.getHomeMap().get(pieceMove.getPiece().getColor());
			pieceMove.getPiece().setToHome(true);
			return;
		}
		// check if it reached its home square or home etc

		Vector3 currentTranslation = new Vector3();
		Vector3 finalTranslation = new Vector3();

		pieceInstance.transform.getTranslation(currentTranslation);

		squareInstMap.get(board.getHomeSquaresMap().get(pieceMove.getPiece().getColor()).get(moveCountHomeSq)).transform
				.getTranslation(finalTranslation);

		Vector3 diff = finalTranslation.sub(currentTranslation);
		modelBatch.render(pieceInstance, environment);
		if (diff.len() < .1f) {
			moveCountHomeSq += 1;
			moveCount++;
		} else {

			pieceInstance.transform.translate(diff.scl(delta * MOVE_SPEED));
		}

	}

	/**
	 * Method to render and animate the movement of a piece from rest area to
	 * start square
	 * 
	 * @param delta
	 */
	public void renderMovingFromRestPiece(float delta) {
		ModelInstance pieceInstance = pieceInstMap.get(pieceMove.getPiece());
		if (restMovedToStart) {
			Vector3 trans = new Vector3();
			squareInstMap.get(board.getSquares().get(moveFinalIndex)).transform.getTranslation(trans);
			// set the destination squares translation to the piece
			pieceInstance.transform.setTranslation(trans);
			Square finalSquare = board.getSquares().get(LudoUtil.getStartIndex(pieceMove.getPiece().getColor()));
			pieceMove.getPiece().setSittingSuare(finalSquare);
			finalSquare.getPieces().add(pieceMove.getPiece());

			pieceMove.getPiece().setRest(false);
			animationComplete = true;
			restMovedToStart = false;
			pieceMove = null;
			return;
		}

		Vector3 currentTranslation = new Vector3();
		Vector3 finalTranslation = new Vector3();

		pieceInstance.transform.getTranslation(currentTranslation);
		squareInstMap.get(board.getSquares().get(moveFinalIndex)).transform.getTranslation(finalTranslation);

		Vector3 diff = finalTranslation.sub(currentTranslation);
		modelBatch.render(pieceInstance, environment);
		if (diff.len() < .1f) {
			restMovedToStart = true;
		} else {

			pieceInstance.transform.translate(diff.scl(delta * MOVE_SPEED));
		}

	}

	public void renderMovingToRestSquare(float delta) {
		ModelInstance pieceInstance = pieceInstMap.get(pieceMove.getPiece());
		if (movedToRest) {
			Vector3 trans = new Vector3();
			squareInstMap.get(moveToRestSq).transform.getTranslation(trans);
			pieceMove.getPiece().getSittingSuare().getPieces().remove(pieceMove.getPiece());
			pieceMove.getPiece().reset();
			pieceMove.getPiece().setSittingSuare(moveToRestSq);
			moveToRestSq.getPieces().add(pieceMove.getPiece());
			pieceMove.getPiece().setRest(true);
			pieceMove.setPiece(null);
			// set the destination squares translation to the piece
			pieceInstance.transform.setTranslation(trans);

			pieceMove = null;
			animationComplete = true;
			movedToRest = false;
			return;
		}
		Vector3 currentTranslation = new Vector3();
		Vector3 finalTranslation = new Vector3();

		pieceInstance.transform.getTranslation(currentTranslation);
		squareInstMap.get(moveToRestSq).transform.getTranslation(finalTranslation);

		Vector3 diff = finalTranslation.sub(currentTranslation);
		modelBatch.render(pieceInstance, environment);
		if (diff.len() < .1f) {
			movedToRest = true;
		} else {

			pieceInstance.transform.translate(diff.scl(delta * MOVE_SPEED));
		}

	}

	public void renderMovingToHome(float delta) {
		ModelInstance pieceInstance = pieceInstMap.get(pieceMove.getPiece());
		if (homeSqMovedToHome) {
			Vector3 trans = new Vector3();
			squareInstMap.get(moveToHome).transform.getTranslation(trans);
			pieceMove.getPiece().getSittingSuare().getPieces().remove(pieceMove.getPiece());
			pieceMove.getPiece().reset();
			pieceMove.getPiece().setMainHome(true);
			pieceMove.getPiece().setSittingSuare(moveToHome);
			moveToHome.getPieces().add(pieceMove.getPiece());
			pieceMove.setPiece(null);
			// set the destination squares translation to the piece
			pieceInstance.transform.setTranslation(trans);

			pieceMove = null;
			animationComplete = true;
			homeSqMovedToHome = false;
			return;
		}

		Vector3 currentTranslation = new Vector3();
		Vector3 finalTranslation = new Vector3();

		pieceInstance.transform.getTranslation(currentTranslation);
		squareInstMap.get(moveToHome).transform.getTranslation(finalTranslation);

		Vector3 diff = finalTranslation.sub(currentTranslation);
		modelBatch.render(pieceInstance, environment);
		if (diff.len() < .1f) {
			homeSqMovedToHome = true;
		} else {

			pieceInstance.transform.translate(diff.scl(delta * MOVE_SPEED));
		}

	}

	public void renderDice(float delta) {
		int count = 0;
		for (Dice dice : diceList) {

			Vector3 translation = new Vector3();
			ModelInstance inst = dice.getDiceInstance();
			inst.transform.getTranslation(translation);

			dice.getDiceInstance().transform.setTranslation(
					(selectedPlayer.getLocX() * Board.DIMENSION * 2 * SQUARE_LENGTH * 1.4f), translation.y,
					(selectedPlayer.getLocY() * Board.DIMENSION * SQUARE_LENGTH * 2 + count * SQUARE_LENGTH * 1.5f)
							- (Board.DIMENSION * SQUARE_LENGTH));

			modelBatch.render(dice.getDiceInstance(), environment);
			count++;

			if (dice.isShake()) {
				inst.transform.getTranslation(translation);
				if (translation.y < 4) {
					inst.transform.translate(0, delta, 0);
				} else {
					translation.y = 2.1f;
					inst.transform.setTranslation(translation);
				}
			}
			// inst.transform.setToRotation(new Vector3(1,0,1),180);
		}

	}

	@Override
	protected void renderPiece(Piece pc, int index, float delta) {
		super.renderPiece(pc, index, delta);

		if (pc.isShake()) {
			ModelInstance inst = pieceInstMap.get(pc);
			Vector3 translation = new Vector3();
			inst.transform.getTranslation(translation);
			// if more then one piece, give some space
			translation.z = translation.z + index;
			if (translation.y < 1) {
				inst.transform.translate(0, delta, 0);
			} else {
				translation.y = 0;
				inst.transform.setTranslation(translation);
			}
		}

	}

	/**
	 * 
	 * @param player
	 * @param move
	 */
	public void setPieceMovingInTrack(Player player, Move move) {
		// resetRenderer();
		moveCount = 0;
		pieceMove = move;
		animationComplete = false;
		if (move == null || move.getPiece() == null) {
			return;
		}
		if (move.getPiece().isRest()) {
			moveFinalIndex = player.getStartIndex();
		} else if (move.getPiece().isKilled()) {
			// moveFinalIndex =
			// getFreeSquare(board.getHomeSquaresMap().get(player.getColor());
		} else if (move.getPiece().isHomeSq()) {
			moveFinalIndex = move.getPiece().getSittingSuare().getIndex() + move.getSquares();
			moveTempIndex = moveCount + 1;
		} else {
			moveFinalIndex = 0;
			moveFinalIndex = LudoUtil.calulateDestIndex(move);
			moveTempIndex = move.getPiece().getSittingSuare().getIndex();
			moveCount = 0;
			moveTempIndex = LudoUtil.calulateNextIndex(move, moveCount);
		}
	}

	public void setPieceMovingOutSideTrack(Move move) {
		// resetRenderer();
		moveCount = 0;
		pieceMove = move;
		animationComplete = false;

		if (move.getPiece().isKilled() || move.getPiece().isJailed()) {
			moveToRestSq = LudoUtil.getFreeRestSquare(move.getPiece().getColor(), board);
		} else if (move.getPiece().isToHome()) {
			moveToHome = board.getHomeMap().get(move.getPiece().getColor());
		} else {

		}
	}

	public Dice createDiceInstance() {
		Map<Integer, ModelInstance> map = new HashMap<Integer, ModelInstance>();
		// create 6 diff dice to avoid rotation complication
		for (int i = 1; i < 7; i++) {
			diceModel = (Model) assetsManager.get("meshes/dice" + i + ".g3dj");
			ModelInstance instance = new ModelInstance(diceModel);
			map.put(i, instance);
			instance.transform.scale(2.5f, 2.5f, 2.5f);
		}
		Dice dice = new Dice();
		dice.setDiceInstanceMap(map);
		return dice;
	}

	public boolean isAnimationComplete() {
		return animationComplete;
	}

	public Map<Piece, ModelInstance> getPieceInstMap() {
		return pieceInstMap;
	}

	public List<Dice> getDiceList() {
		return diceList;
	}

	public Player getSelectedPlayer() {
		return selectedPlayer;
	}

	public void setSelectedPlayer(Player selectedPlayer) {
		this.selectedPlayer = selectedPlayer;
	}

}
