package com.vp.game.trajectories;

import com.vp.game.units.Unit;

public abstract class Trajectory {	
	//Unit which pose is to be updated
	Unit unit;
	//indicates if the Trajectory has finished
	public boolean finished;
	
	protected Trajectory(Unit unit){
		this.unit = unit;
	}
	//Updates the current position of the unit on the trajectory
	public abstract void update(float delta);
}
