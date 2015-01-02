package com.vp.game.units;

import com.badlogic.gdx.graphics.g3d.Model;
import com.vp.game.trajectories.CirclesegLineTrajectory;

public class Ninja extends Unit {
	
	public final static float STANDARD_ROTATERADIUS = 25;
	public final static float STANDARD_RADIUS = 10;
	public final static float STANDARD_SPEED = 100;
	public final static String START_ANIMATION = "Take 001";
	
	public static Ninja mainNinja;
	private static Model model;
	
	public final CirclesegLineTrajectory tra;
	public float rotateRadius;
	//Is set to true if the ninja is being guided by the Wall
	public boolean isWallGuided;
	public boolean isTopWallGuided;
	public boolean isBottomWallGuided;
	
	//Indicates if the ninja can collide with Obstacles
	public boolean obsCollideAble;
	//Indicates if the ninja can collide with anything
	public boolean collideAble;
	
	public final ActiveItemsManager activeItems;
	
	public Ninja(float positionX, float positionY, float positionZ, float directionX, float directionY, float directionZ, float speed, float rotateRadius, float radius) {
		super(positionX, positionY, positionZ, directionX, directionZ, speed, radius);
		this.rotateRadius = rotateRadius;
		this.obsCollideAble = true;
		this.collideAble = true;
		tra = new CirclesegLineTrajectory(this);
		((CirclesegLineTrajectory) tra).setTrajectory(this.direction.x, this.direction.y);
		activeItems = new ActiveItemsManager();
	}
	
	public Ninja(float positionX, float positionY, float positionZ, float directionX, float directionY, float directionZ) {
		super(positionX, positionY, positionZ, directionX, directionZ, Ninja.STANDARD_SPEED, Ninja.STANDARD_RADIUS, Ninja.START_ANIMATION);
		this.rotateRadius = Ninja.STANDARD_ROTATERADIUS;
		this.obsCollideAble = true;
		this.collideAble = true;
		tra = new CirclesegLineTrajectory(this);
		((CirclesegLineTrajectory) tra).setTrajectory(this.direction.x, this.direction.y);
		activeItems = new ActiveItemsManager();
	}

	public Ninja(float positionX, float positionY, float positionZ, float directionX, float directionY, float directionZ, float speed, float rotateRadius, float radius, String animation) {
		super(positionX, positionY, positionZ, directionX, directionZ, speed, radius,
				animation);
		this.rotateRadius = rotateRadius;
		this.obsCollideAble = true;
		this.collideAble = true;
		tra = new CirclesegLineTrajectory(this);
		((CirclesegLineTrajectory) tra).setTrajectory(this.direction.x, this.direction.y);
		activeItems = new ActiveItemsManager();
	}

	@Override
	public void update(float delta) {
		tra.update(delta);	
		activeItems.update(delta);
	}

	public static void setModel(Model setModel) {
		model = setModel;		
	}

	@Override
	public Model getModel() {
		return model;
	}
}
