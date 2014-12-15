package com.vp.game.obstacleupdating;

import java.util.List;

import com.vp.game.collisionmanaging.CollisionManager;
import com.vp.game.gameelements.Chunk;
import com.vp.game.tools.WrappingArray;
import com.vp.game.trajectories.TurnFiniteLineTrajectory;
import com.vp.game.units.Obstacle;
import com.vp.game.units.Unit;
import com.vp.game.units.Wolf;
import com.vp.game.updaterangemanaging.UpdateRangeManager;
import com.vp.game.worldelements.Walls;
import com.vp.game.worldelements.WorldElement;

public class ObstacleUpdater implements WorldElement{
	private final CollisionManager colManager;
	private final UpdateRangeManager upRManager;
	private final Walls walls;
	
	public ObstacleUpdater(CollisionManager colManager, UpdateRangeManager upRManager, Walls walls) {
		super();
		this.colManager = colManager;
		this.upRManager = upRManager;
		this.walls = walls;
	}

	@Override
	public void update(float delta) {
		for (int i = 0; i < Unit.unitsInRange.size; i++) {
			final Wolf wolf = (Wolf) Unit.unitsInRange.get(i);
			
			//Set new Trajectory if finished
			if(wolf.tra.finished){
				float turnAngle = (float)((Math.random() * 2) * Math.PI);
				((TurnFiniteLineTrajectory) wolf.tra).setTrajectory((float)Math.random()*100, turnAngle);
			}
			
			//Save old position then update
			float posXOld = wolf.position.x;
			float posYOld = wolf.position.y;
			wolf.tra.update(delta);
			
			//Check collisions
			if(colManager.checkCollisions(wolf) || wolf.position.y < walls.maxTopPosition || wolf.position.y >= walls.maxBottomPosition){
				wolf.position.x = posXOld;
				wolf.position.y = posYOld;
				wolf.tra.finished = true;
			}
			
			//Check if still in update range
			if(wolf.position.x < upRManager.updateRangeXStart || wolf.position.x >= upRManager.updateRangeXEnd){
				upRManager.removeFromRange(wolf);
			}			
		}
	}

}
