package com.vp.game.obstacleupdating;

import java.util.List;

import com.vp.game.collisionmanaging.CollisionManager;
import com.vp.game.gameelements.Chunk;
import com.vp.game.tools.WrappingArray;
import com.vp.game.trajectories.TurnFiniteLineTrajectory;
import com.vp.game.units.Obstacle;
import com.vp.game.units.Unit;
import com.vp.game.units.Wolf;
import com.vp.game.worldelements.WorldElement;

public class ObstacleUpdater implements WorldElement{
	private final CollisionManager colManager;
	
	public ObstacleUpdater(CollisionManager colManager) {
		super();
		this.colManager = colManager;
	}

	@Override
	public void update(float delta) {
		for (int i = 0; i < Unit.unitsInRange.size; i++) {
			Wolf wolf = (Wolf) Unit.unitsInRange.get(i);
			if(wolf.tra.finished){
				float turnAngle = (float)((Math.random() * 2 - 1) * Math.PI);
				((TurnFiniteLineTrajectory) wolf.tra).setTrajectory((float)Math.random()*100, turnAngle);
			}
			float posXOld = wolf.position.x;
			float posYOld = wolf.position.y;
			wolf.tra.update(delta);
			if(!Obstacle.spatialHashGrid.move(wolf, wolf.position.x, wolf.position.y, wolf.globalChunkID, wolf.xPositionID, wolf.yPositionID)){
				Unit.unitsInRange.removeIndex(i);
				i--;
			}else{
				if(colManager.checkCollisions(wolf)){
					wolf.position.x = posXOld;
					wolf.position.y = posYOld;
					float turnAngle = (float)((Math.random() * 2 - 1) * Math.PI);
					((TurnFiniteLineTrajectory) wolf.tra).setTrajectory((float)Math.random()*100, turnAngle);
				}
			}			
		}
	}

}
