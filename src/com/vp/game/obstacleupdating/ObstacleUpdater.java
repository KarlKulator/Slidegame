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
			final Unit unit = Unit.unitsInRange.get(i);
			if(unit instanceof Obstacle){
				final Wolf wolf = (Wolf) unit;
				
				//Set new Trajectory if finished
				if(wolf.tra.finished){
					float turnAngle = (float)((Math.random() * 2) * Math.PI);
					((TurnFiniteLineTrajectory) wolf.tra).setTrajectory((float)Math.random()*400, turnAngle);
				}
				
				//Save old position then update
				float posXOld = wolf.position.x;
				float posYOld = wolf.position.y;
				wolf.tra.update(delta);
				
				//Check collisions
				if(wolf.position.y < walls.maxTopPosition || wolf.position.y >= walls.maxBottomPosition || colManager.checkCollisions(wolf)){
					wolf.move(posXOld, posYOld);
					wolf.tra.finished = true;
				}		
			}else{
				unit.update(delta);
			}
			//Check if still in update range
			if(unit.position.x < upRManager.updateRangeXStart || unit.position.x >= upRManager.updateRangeXEnd){
				upRManager.removeFromRange(unit);
			}
		}
	}

}
