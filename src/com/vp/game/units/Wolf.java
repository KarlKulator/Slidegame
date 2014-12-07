package com.vp.game.units;

import com.badlogic.gdx.utils.Pool;
import com.vp.game.trajectories.TurnFiniteLineTrajectory;
import com.vp.game.trajectories.Trajectory;


public class Wolf extends Obstacle {
	
	public final static float STANDARD_RADIUS = 9; 
	public final static float STANDARD_TURNSPEED = 0.5f;
	
	public final static Pool<Wolf> pool = new Pool<Wolf>(){
		@Override
		protected Wolf newObject(){
			System.out.println("new" + pool.getFree());
			return new Wolf();
		}
	};
	
	public Trajectory tra = new TurnFiniteLineTrajectory(this);
	
	public Wolf(float positionX, float positionY, float positionZ,
			float directionX, float directionY, float directionZ, float speed,
			float radius) {
		super(positionX, positionY, positionZ, directionX, directionY, directionZ,
				speed, radius);
	}
	
	public Wolf(float positionX, float positionY, float positionZ,
			float directionX, float directionY, float directionZ, float speed) {
		super(positionX, positionY, positionZ, directionX, directionY, directionZ,
				speed, STANDARD_RADIUS);
	}
	
	public Wolf() {
		super(STANDARD_RADIUS);
		this.turnSpeed = STANDARD_TURNSPEED;
	}

	@Override
	public void update(float delta) {
		tra.update(delta);
	}

	@Override
	public void free() {
		Wolf.pool.free(this);
	}

}
