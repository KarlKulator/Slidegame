package com.vp.game.trajectories;

import com.vp.game.units.Unit;

public class TurnFiniteLineTrajectory extends Trajectory {
	
	private float distanceMoved;
	private float distanceToMove;
	
	private boolean turning;
	private float angleToTurn;
	private float angleTurned;
	
	public TurnFiniteLineTrajectory(Unit unit){
		super(unit);
		finished = true;
		turning = false;
	}
	
	public void setTrajectory(float distanceToMove, float angleToTurn){
		this.distanceToMove = distanceToMove;
		this.distanceMoved = 0;
		this.angleToTurn = angleToTurn;
		this.angleTurned = 0;
		finished = false;
		turning = true;
	}
	
	@Override
	public void update(float delta) {
		if(!finished){
			if(turning){
				float angleTurnedInThisStep = delta * unit.turnSpeed;
				angleTurned += angleTurnedInThisStep;
				if(angleTurned < angleToTurn){
					unit.direction.rotateRad(angleTurnedInThisStep);
				}else{
					turning = false;
				}				
			}else{
				float distanceMovedInThisStep = delta*unit.speed;
				distanceMoved += distanceMovedInThisStep;
				if(distanceMoved < distanceToMove){				
					float newPositionX = unit.position.x + unit.direction.x*distanceMovedInThisStep;
					float newPositionY =  unit.position.y + unit.direction.y*distanceMovedInThisStep;
					unit.move(newPositionX, newPositionY);
				}else{
					finished = true;
				}
			}
		}		
	}

}
