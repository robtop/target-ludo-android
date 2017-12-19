package com.tgt.ludo.ui;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3;
import com.tgt.ludo.board.Board;

/***
 * Render the game action like kills, jail and home events
 * 
 * @author robin
 *
 */
public class ActiveGameBoardRenderer extends BoardRenderer {


	public ActiveGameBoardRenderer(Board board, RenderContext renderContext, PerspectiveCamera cam, Environment environment) {
		super(board, renderContext, cam, environment);
		
	}

	public void render(float delta) {
		super.render(delta);

	}

	public void renderMovingJailPiece(float delta) {
		ModelInstance pieceInstance = pieceInstMap.get(pieceMove.getPiece());
		if (restMovedToStart) {
			Vector3 trans = new Vector3();
			squareInstMap.get(board.getSquares().get(moveFinalIndex)).transform.getTranslation(trans);
			// set the destination squares translation to the piece
			pieceInstance.transform.setTranslation(trans);
			animationComplete = true;
			restMovedToStart = false;
			return;
		}

		Vector3 currentTranslation = new Vector3();
		Vector3 finalTranslation = new Vector3();
		pieceInstance = pieceInstMap.get(pieceMove.getPiece());
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
	

	public void renderMovingKilledPiece(float delta) {
		ModelInstance pieceInstance = pieceInstMap.get(pieceMove.getPiece());
		if (restMovedToStart) {
			Vector3 trans = new Vector3();
			squareInstMap.get(board.getSquares().get(moveFinalIndex)).transform.getTranslation(trans);
			// set the destination squares translation to the piece
			pieceInstance.transform.setTranslation(trans);
			animationComplete = true;
			restMovedToStart = false;
			return;
		}

		Vector3 currentTranslation = new Vector3();
		Vector3 finalTranslation = new Vector3();
		pieceInstance = pieceInstMap.get(pieceMove.getPiece());
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
	


}