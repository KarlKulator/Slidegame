package com.vp.game.units;

import com.vp.game.trajectories.CirclesegLineTrajectory;

public class Ninja extends Unit {

	public final CirclesegLineTrajectory tra;
	public float rotateRadius;
	//Is set to true if the ninja is being guided by the Wall
	public boolean isWallGuided;
	public boolean isTopWallGuided;
	public boolean isBottomWallGuided;

	public Ninja(float positionX, float positionY, float positionZ, float directionX, float directionY, float directionZ, float speed, float rotateRadius, float radius) {
		super(positionX, positionY, positionZ, directionX, directionZ, speed, radius);
		this.rotateRadius = rotateRadius;
		this.radius = radius;
		tra = new CirclesegLineTrajectory(this);
		((CirclesegLineTrajectory) tra).setTrajectory(this.direction.x, this.direction.y);
	}

	public Ninja(float positionX, float positionY, float positionZ, float directionX, float directionY, float directionZ, float speed, float rotateRadius, float radius, String animation) {
		super(positionX, positionY, positionZ, directionX, directionZ, speed, radius,
				animation);
		this.radius = radius;
		this.rotateRadius = rotateRadius;
		tra = new CirclesegLineTrajectory(this);
		((CirclesegLineTrajectory) tra).setTrajectory(this.direction.x, this.direction.y);
	}

	@Override
	public void update(float delta) {
		tra.update(delta);		
	}
}
