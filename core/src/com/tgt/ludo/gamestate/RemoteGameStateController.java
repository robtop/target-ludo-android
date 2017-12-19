package com.tgt.ludo.gamestate;

import com.badlogic.gdx.Screen;

/**
 * Supports remote sessions
 * @author robin
 *
 */
public class RemoteGameStateController extends LudoGameStateController{
	
	public RemoteGameStateController(Screen screen) {
		super(screen);
		// TODO Auto-generated constructor stub
	}

	public void update() {
		super.update();
		updateServer();
	}

	private void updateServer() {

	}

	
}
