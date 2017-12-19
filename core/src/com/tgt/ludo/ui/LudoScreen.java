package com.tgt.ludo.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3;
import com.tgt.ludo.gamestate.DataCollectorGameStateController;
import com.tgt.ludo.gamestate.GameStateController;
import com.tgt.ludo.player.Player;

public class LudoScreen implements Screen {

	public PerspectiveCamera cam;
	// for 2D
	OrthographicCamera guiCam;
	public Environment environment;
	public CameraInputController camController;
	public Shader shader;
	public RenderContext renderContext;
	Renderable renderable;

	private float i = 0;
	protected GL20 gl20 = null;

	private BoardRenderer boardRenderer;
	protected Vector3 touchPoint;
	GameStateController ludoGameState;
	
	public static final Vector3 LIGHT_DIRECTION = new Vector3(-1f, 1f, 3f);
	public void create() {

		gl20 = Gdx.app.getGraphics().getGL20();
		gl20.glEnable(GL20.GL_TEXTURE_2D);
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.9f,
				0.9f, 0.9f, 1f));
		environment.add(new DirectionalLight().set(.6f, .6f, .6f,
				LIGHT_DIRECTION.x, LIGHT_DIRECTION.y, LIGHT_DIRECTION.z));
		// 3d Camera setup
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.far = 200;
		cam.position.set(30f,80f, 5f);
		cam.lookAt(30f, 0f, 6f);
//		cam.position.set(-30f, 30f, -5f);
//		cam.lookAt(100, -50, 0);
		cam.update();

		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);

		// allow to move the camera with the mouse
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		touchPoint = new Vector3();

		//ludoGameState = new AdvancedLudoGameStateController((Screen) this);
		ludoGameState = new DataCollectorGameStateController((Screen) this);
		
		boardRenderer = new BoardRenderer(ludoGameState.getBoard(), renderContext, cam, environment);
		
		Player selectedPlayer = ludoGameState.getPlayers().get(0);
		selectedPlayer.setTurn(true);
		boardRenderer.setSelectedPlayer(selectedPlayer);
	}

	public void render(float delta) {
		camController.update();
		update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		float deltaTime = Gdx.graphics.getDeltaTime();
		i = i + deltaTime * 200;
		if (i >= 512) {
			i = 0;
		}

		boardRenderer.render(delta);
	}

	public void dispose() {
		boardRenderer.dispose();
	}

	public void update() {
		ludoGameState.update();
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		}
		// any screen inputs?
	}

	public PerspectiveCamera getCam() {
		return cam;
	}

	public void setCam(PerspectiveCamera cam) {
		this.cam = cam;
	}

	public OrthographicCamera getGuiCam() {
		return guiCam;
	}

	public void setGuiCam(OrthographicCamera guiCam) {
		this.guiCam = guiCam;
	}

	public void resume() {

	}

	@Override
	public void resize(int x, int y) {

	}

	public void hide() {

	}

	public void show() {

	}

	public void pause() {

	}

	public BoardRenderer getBoardRenderer() {
		return boardRenderer;
	}
	
}
