package com.vp.game.units;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Pool;
import com.vp.game.trajectories.TurnFiniteLineTrajectory;
import com.vp.game.trajectories.Trajectory;


public class Wolf extends Obstacle {
	
	public final static float STANDARD_RADIUS = 10; 
	public final static float STANDARD_TURNSPEED = 1f;
	
	private static Model model;
	public final static Pool<Wolf> pool = new Pool<Wolf>(){
		@Override
		protected Wolf newObject(){
			System.out.println("new" + pool.getFree());
			return new Wolf();
		}
	};
	
	public Trajectory tra = new TurnFiniteLineTrajectory(this);
	
	public Wolf() {
		super(STANDARD_RADIUS, "Take 001");
		this.turnSpeed = STANDARD_TURNSPEED;
	}

	@Override
	public void update(float delta) {
		tra.update(delta);
	}

	@Override
	public void free() {
		super.free();
		Wolf.pool.free(this);
	}
	
	public static void setModel(Model setModel) {
		model = setModel;		
	}

	@Override
	public Model getModel() {
		return model;
	}

}
