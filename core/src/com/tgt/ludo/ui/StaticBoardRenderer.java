package com.tgt.ludo.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3;
import com.tgt.ludo.board.Board;
import com.tgt.ludo.board.Board.COLOR;
import com.tgt.ludo.board.Piece;
import com.tgt.ludo.board.Square;

public class StaticBoardRenderer {

	protected RenderContext renderContext;
	protected ModelBatch modelBatch;
	protected PerspectiveCamera cam;
	protected Board board;
	public static AssetManager assetsManager = new AssetManager();
	public static final int SQUARE_LENGTH = 4;
	protected Model squareModel;
	protected Model greenPawnModel;
	protected Model yellowPawnModel;
	protected Model redPawnModel;
	protected Model bluePawnModel;

	protected Model squareRedModel;
	protected Model squareGreenModel;
	protected Model squareYellowModel;
	protected Model squareBlueModel;

	protected Model squareJailModel;
	protected Model squareHomeModel;

	protected Model baseModel;

	public Environment environment;
	// keep ModelInstance here instead of in the Square to keep the backend
	// independent of the UI
	protected Map<Square, ModelInstance> squareInstMap;
	protected Map<Piece, ModelInstance> pieceInstMap;

	protected ModelInstance base;

	public StaticBoardRenderer(Board board, RenderContext renderContext, PerspectiveCamera cam,
			Environment environment) {
		this.board = board;
		this.renderContext = renderContext;
		this.cam = cam;
		this.environment = environment;

		assetsManager.load("meshes/square_white.g3dj", Model.class);

		assetsManager.load("meshes/square_red.g3dj", Model.class);
		assetsManager.load("meshes/square_green.g3dj", Model.class);
		assetsManager.load("meshes/square_yellow.g3dj", Model.class);
		assetsManager.load("meshes/square_blue.g3dj", Model.class);

		assetsManager.load("meshes/square_jail.g3dj", Model.class);
		assetsManager.load("meshes/square_home.g3dj", Model.class);

		assetsManager.load("meshes/base.g3dj", Model.class);

		assetsManager.load("meshes/pawnGreen.g3dj", Model.class);
		assetsManager.load("meshes/pawnYellow.g3dj", Model.class);
		assetsManager.load("meshes/pawnRed.g3dj", Model.class);
		assetsManager.load("meshes/pawnBlue.g3dj", Model.class);
		assetsManager.finishLoading();
		squareModel = (Model) assetsManager.get("meshes/square_white.g3dj");

		baseModel = (Model) assetsManager.get("meshes/base.g3dj");

		squareRedModel = (Model) assetsManager.get("meshes/square_red.g3dj");
		squareGreenModel = (Model) assetsManager.get("meshes/square_green.g3dj");
		squareYellowModel = (Model) assetsManager.get("meshes/square_yellow.g3dj");
		squareBlueModel = (Model) assetsManager.get("meshes/square_blue.g3dj");

		squareJailModel = (Model) assetsManager.get("meshes/square_jail.g3dj");
		squareHomeModel = (Model) assetsManager.get("meshes/square_home.g3dj");

		greenPawnModel = (Model) assetsManager.get("meshes/pawnGreen.g3dj");
		yellowPawnModel = (Model) assetsManager.get("meshes/pawnYellow.g3dj");
		redPawnModel = (Model) assetsManager.get("meshes/pawnRed.g3dj");
		bluePawnModel = (Model) assetsManager.get("meshes/pawnBlue.g3dj");

		squareInstMap = new HashMap<Square, ModelInstance>();
		pieceInstMap = new HashMap<Piece, ModelInstance>();
		createOuterTrack();
		createHomeSquares();
		createRestSquares();
		createBase();
		createMainHome();
		modelBatch = new ModelBatch();
	}

	/**
	 * Colored squares that lead to the central home
	 * 
	 * @param delta
	 */
	protected void renderHomeSquares(float delta) {
		for (Square sq : board.getHomeSquaresMap().get(COLOR.GREEN)) {
			renderSquare(sq, delta);
		}
		for (Square sq : board.getHomeSquaresMap().get(COLOR.YELLOW)) {
			renderSquare(sq, delta);
		}
		for (Square sq : board.getHomeSquaresMap().get(COLOR.RED)) {
			renderSquare(sq, delta);
		}
		for (Square sq : board.getHomeSquaresMap().get(COLOR.BLUE)) {
			renderSquare(sq, delta);
		}
	}

	protected void renderRestSquares(float delta) {
		for (Square sq : board.getRestSquaresMap().get(COLOR.GREEN)) {
			renderSquare(sq, delta);
		}
		for (Square sq : board.getRestSquaresMap().get(COLOR.YELLOW)) {
			renderSquare(sq, delta);
		}
		for (Square sq : board.getRestSquaresMap().get(COLOR.RED)) {
			renderSquare(sq, delta);
		}
		for (Square sq : board.getRestSquaresMap().get(COLOR.BLUE)) {
			renderSquare(sq, delta);
		}
	}

	public void render(float delta) {

		renderContext.begin();
		modelBatch.begin(cam);
		renderOuterTrack(delta);
		renderHomeSquares(delta);
		renderRestSquares(delta);
		renderBase();
		renderMainHome(delta);
		modelBatch.end();
		renderContext.end();

	}

	private void renderOuterTrack(float delta) {
		for (Square sq : board.getSquares()) {
			renderSquare(sq, delta);
		}
	}

	/**
	 * Center home squares
	 */
	private void renderMainHome( float delta) {
		modelBatch.render(squareInstMap.get(board.getHomeMap().get(COLOR.GREEN)), environment);
		renderPiecesInSquare(board.getHomeMap().get(COLOR.GREEN), delta);
		modelBatch.render(squareInstMap.get(board.getHomeMap().get(COLOR.YELLOW)), environment);
		renderPiecesInSquare(board.getHomeMap().get(COLOR.YELLOW), delta);
		
		modelBatch.render(squareInstMap.get(board.getHomeMap().get(COLOR.RED)), environment);
		renderPiecesInSquare(board.getHomeMap().get(COLOR.RED), delta);
		modelBatch.render(squareInstMap.get(board.getHomeMap().get(COLOR.BLUE)), environment);
		renderPiecesInSquare(board.getHomeMap().get(COLOR.BLUE), delta);
	}

	protected void renderSquare(Square sq, float delta) {
		modelBatch.render(squareInstMap.get(sq), environment);
		renderPiecesInSquare(sq, delta);
	}
	
	private void renderPiecesInSquare(Square sq, float delta){
		if (sq.getPieces() != null && !sq.getPieces().isEmpty()) {

			for (int i = 0; i < sq.getPieces().size(); i++) {
				Piece pc = sq.getPieces().get(i);
				if(pc.getSittingSuare()!=sq){
					//TODO: create cutom exception class
					//throw new RuntimeException("Something wrong in sitting piece!");
				}
				renderPiece(pc, i, delta);
			}
		}
	}

	protected void renderPiece(Piece pc, int index, float delta) {
		//Vector3 translation = new Vector3();
		ModelInstance inst = pieceInstMap.get(pc);
		// if more then one piece, give some space
		//translation.set(translation.x+index*2, translation.y+index*2, translation.z);
		//inst.transform.translate(translation);
		modelBatch.render(inst, environment);
		//translation.z = translation.z + 1;
	}

	private void createBase() {
		base = new ModelInstance(baseModel);
		base.transform.translate(40, -2, 4);
		base.transform.scale(1.31f, 1.31f, 1.31f);
	}

	private void renderBase() {
		modelBatch.render(base, environment);

	}

	/**
	 * Create the 3D models of the individual squares and translate them to
	 * their positions
	 */
	protected void createOuterTrack() {
		int xTranslation = 0;
		int yTranslation = 0;
		int xControl = 1;
		int yControl = 0;
		ModelInstance instance = null;
		for (Square sq : board.getSquares()) {
			// pick different model based on square type or use shader to color
			if (sq.isHome()) {
				instance = new ModelInstance(squareHomeModel);
			} else if (sq.isJail()) {
				instance = new ModelInstance(squareJailModel);
			} else if (sq.isStartSquare()) {
				for (int i = 0; i < 4; i++) {
					if (Board.startIndexes.get(i).equals(sq.getIndex())) {
						switch (i) {
						case 0:
							instance = new ModelInstance(squareGreenModel);
							break;
						case 1:
							instance = new ModelInstance(squareYellowModel);
							break;
						case 2:
							instance = new ModelInstance(squareRedModel);
							break;
						case 3:
							instance = new ModelInstance(squareBlueModel);
							break;
						}
					}

				}
			} else {
				instance = new ModelInstance(squareModel);
			}
			// instance.transform.scale(.9f, .9f, .9f);
			if (sq.getIndex() == Board.DIMENSION) {
				xTranslation += xControl * SQUARE_LENGTH;
				xControl = 0;
				yControl = -1;
			}
			// left wing end
			if (sq.getIndex() == Board.DIMENSION * 2) {
				xControl = 1;
				yControl = 0;
			}
			// left wing top
			if (sq.getIndex() == Board.DIMENSION * 2 + 2) {
				xControl = 0;
				yControl = 1;
			}
			// top wing start
			if (sq.getIndex() == Board.DIMENSION * 3 + 1) {
				yTranslation += yControl * SQUARE_LENGTH;
				xControl = 1;
				yControl = 0;
			}
			// top wing end
			if (sq.getIndex() == Board.DIMENSION * 4 + 1) {
				xControl = 0;
				yControl = 1;
			}

			if (sq.getIndex() == Board.DIMENSION * 4 + 1 + 2) {
				xControl = -1;
				yControl = 0;
			}
			// right wing start
			if (sq.getIndex() == Board.DIMENSION * 5 + 1 + 1) {
				xTranslation += xControl * SQUARE_LENGTH;
				xControl = 0;
				yControl = 1;
			}
			// right wing top
			if (sq.getIndex() == Board.DIMENSION * 6 + 2) {
				xControl = -1;
				yControl = 0;
			}
			if (sq.getIndex() == Board.DIMENSION * 6 + 2 + 2) {
				xControl = 0;
				yControl = -1;
			}
			if (sq.getIndex() == Board.DIMENSION * 7 + 3) {
				yTranslation += yControl * SQUARE_LENGTH;
				xControl = -1;
				yControl = 0;
			}
			if (sq.getIndex() == Board.DIMENSION * 8 + 3) {
				xControl = 0;
				yControl = -1;
			}

			xTranslation += xControl * SQUARE_LENGTH;
			yTranslation += yControl * SQUARE_LENGTH;
			instance.transform.translate(new Vector3(xTranslation, 0, yTranslation));
			squareInstMap.put(sq, instance);

		}

	}

	protected ModelInstance createPieceInstance(Piece piece, COLOR color) {
		ModelInstance instance = null;
		switch (color) {
		case GREEN:
			instance = new ModelInstance(greenPawnModel);
			break;

		case YELLOW:
			instance = new ModelInstance(yellowPawnModel);
			break;

		case RED:
			instance = new ModelInstance(redPawnModel);
			break;
		case BLUE:
			instance = new ModelInstance(bluePawnModel);
			break;
		}

		pieceInstMap.put(piece, instance);
		return instance;
	}

	protected void createHomeSquares() {

		createHomeSquares(squareGreenModel, COLOR.GREEN, new Vector3(2 * SQUARE_LENGTH, 1, 1 * SQUARE_LENGTH), 1, 0);

		createHomeSquares(squareYellowModel, COLOR.YELLOW,
				(new Vector3((Board.DIMENSION + 2) * SQUARE_LENGTH, 0, (-1) * SQUARE_LENGTH)), 0, -1);

		createHomeSquares(squareBlueModel, COLOR.BLUE,
				new Vector3((Board.DIMENSION + 2) * SQUARE_LENGTH, 0, (3) * SQUARE_LENGTH), 0, 1);

		createHomeSquares(squareRedModel, COLOR.RED,
				new Vector3((Board.DIMENSION *2 + 2) * SQUARE_LENGTH, 0, 1 * SQUARE_LENGTH), -1, 0);
	}

	private void createHomeSquares(Model squareModel, COLOR color, Vector3 translation, int xControl, int yControl) {

		for (Square sq : board.getHomeSquaresMap().get(color)) {
			ModelInstance instance = new ModelInstance(squareModel);
			squareInstMap.put(sq, instance);
			instance.transform.translate(translation);
			translation.x += xControl * SQUARE_LENGTH;
			translation.z += yControl * SQUARE_LENGTH;
		}

	}

	private void createMainHome() {
		ModelInstance instance = new ModelInstance(squareGreenModel);
		instance.transform.translate((Board.DIMENSION + 1) * SQUARE_LENGTH, .6f, SQUARE_LENGTH);
		instance.transform.scale(1.5f, 1.5f, 1.5f);
		squareInstMap.put(board.getHomeMap().get(COLOR.GREEN), instance);
		
		instance = new ModelInstance(squareYellowModel);
		instance.transform.translate((Board.DIMENSION + 2) * SQUARE_LENGTH, .6f, 0);
		instance.transform.scale(1.5f, 1.5f, 1.5f);
		squareInstMap.put(board.getHomeMap().get(COLOR.YELLOW), instance);
		
		instance = new ModelInstance(squareBlueModel);
		instance.transform.translate((Board.DIMENSION + 2) * SQUARE_LENGTH, .6f, 2 * SQUARE_LENGTH);
		instance.transform.scale(1.5f, 1.5f, 1.5f);
		squareInstMap.put(board.getHomeMap().get(COLOR.BLUE), instance);
		
		instance = new ModelInstance(squareRedModel);
		instance.transform.translate((Board.DIMENSION + 3) * SQUARE_LENGTH, .6f, 1 * SQUARE_LENGTH);
		instance.transform.scale(1.5f, 1.5f, 1.5f);
		squareInstMap.put(board.getHomeMap().get(COLOR.RED), instance);
	}

	protected void createRestSquares() {

		createRestSquares(COLOR.GREEN, new Vector3(2 * SQUARE_LENGTH, 0, -5 * SQUARE_LENGTH));
		createRestSquares(COLOR.YELLOW, new Vector3(2 * Board.DIMENSION * SQUARE_LENGTH, 0, -5 * SQUARE_LENGTH));
		createRestSquares(COLOR.RED, new Vector3(2 * Board.DIMENSION * SQUARE_LENGTH, 0, 5 * SQUARE_LENGTH));
		createRestSquares(COLOR.BLUE, new Vector3(2 * SQUARE_LENGTH, 0, 5 * SQUARE_LENGTH));
	}

	private void createRestSquares(COLOR color, Vector3 translation) {
		int xControl = 1;
		int yControl = 0;

		for (Square sq : board.getRestSquaresMap().get(color)) {
			ModelInstance instance = new ModelInstance(squareModel);

			squareInstMap.put(sq, instance);
			instance.transform.translate(translation);

			int temp = xControl;
			xControl = yControl;
			yControl = temp;
			if (sq.getPieces() != null && !sq.getPieces().isEmpty()) {
				ModelInstance pieceInstance = createPieceInstance(sq.getPieces().get(0), color);
				Vector3 translationTemp = translation.cpy();
				translationTemp.y = translationTemp.y + 1;
				pieceInstance.transform.translate(translationTemp);
			}

			translation.x += xControl * SQUARE_LENGTH * 2;
			translation.z += yControl * SQUARE_LENGTH * 2;
		}

	}

	public void dispose() {
		for (Square sq : squareInstMap.keySet()) {
			//squareInstMap.get(sq).model.dispose();
		}
		for (Piece pc : pieceInstMap.keySet()) {
		//squareInstMap.get(pc).model.dispose();
		}
		assetsManager.clear();
		assetsManager.dispose();
	}
}
