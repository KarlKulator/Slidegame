package com.vp.game.obstacleupdating;

import java.util.List;

import com.vp.game.gameelements.Chunk;
import com.vp.game.tools.WrappingArray;
import com.vp.game.trajectories.TurnFiniteLineTrajectory;
import com.vp.game.units.Obstacle;
import com.vp.game.units.Wolf;
import com.vp.game.worldelements.WorldElement;

public class ObstacleUpdater implements WorldElement{
	//The Chunks(WrappingArray) where the obstacles are on
	private final WrappingArray<Chunk> chunks;
	
	public ObstacleUpdater(WrappingArray<Chunk> chunks) {
		this.chunks = chunks;
	}

	@Override
	public void update(float delta) {
		int chunkAmount = chunks.getSize();
		for(int i = 0; i < chunkAmount; i++){
			List<Obstacle> obstacles = chunks.get(i).obstacles;
			int obstaclesOnChunk = obstacles.size();
			for(int j = 0 ; j < obstaclesOnChunk; j++){
				Obstacle obs = obstacles.get(j);
				if(obs instanceof Wolf){
					Wolf wolf = ((Wolf) obs);
					TurnFiniteLineTrajectory wolfTra = (TurnFiniteLineTrajectory) wolf.tra;
					if(wolfTra.finished){
						float turnAngle = (float)((Math.random() * 2 - 1) * Math.PI);
						wolfTra.setTrajectory((float)Math.random()*100, turnAngle);
					}
				}
				obstacles.get(j).update(delta);
			}
		}
	}

}
