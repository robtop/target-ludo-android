package com.tgt.ludo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.tgt.ludo.ui.LudoScreen;

public class LudoGame extends ApplicationAdapter {
	
    private LudoScreen ludoScreen;

    
	@Override
	public void create() {
		
		ludoScreen = new LudoScreen();
		ludoScreen.create();
	}

	@Override
	public void render() {
		ludoScreen.render(Gdx.graphics.getDeltaTime());
	}
	

	@Override
	public void dispose() {
		ludoScreen.dispose();
		super.dispose();
	}
}
