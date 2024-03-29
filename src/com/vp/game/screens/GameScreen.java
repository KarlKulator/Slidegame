package com.vp.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.vp.game.SlideGame;
import com.vp.game.gamecore.Renderer;
import com.vp.game.gamecore.Simulation;

public class GameScreen implements Screen {
	
	final private SlideGame game;
	final private Simulation sim;
	final private Renderer rend;
	
	final private PerspectiveCamera cam;
	
	public GameScreen(final SlideGame gam) {
		this.game = gam;
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(0, 150, 70);
		cam.lookAt(0, 0, 20);
		cam.near = 1f;
		cam.far = 1000f;
		cam.update();
	//	System.out.println("Renderer");
		rend = new Renderer();
	//	System.out.println("Simulation");
		sim = new Simulation(cam);
		rend.setSim(sim);
		Gdx.input.setInputProcessor(sim);	
	//	System.out.println("ready");
	}
	@Override
	public void render(float delta) {
		if(delta<1.0f/10){
		//	long startTime = System.nanoTime();
			if(!sim.update(delta)){
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		//	System.out.println("update: " + (System.nanoTime() - startTime)/1000);
		//	startTime = System.nanoTime();
			rend.render(delta);
		//	System.out.println("render: " + (System.nanoTime() - startTime)/1000);
		}
	}

	@Override
	public void resize(int width, int height) {
		rend.resize(width, height);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
