package com.vp.game.chunkmanaging;

import com.vp.game.collisionmanaging.CollisionManager;
import com.vp.game.gameelements.Chunk;
import com.vp.game.units.Obstacle;
import com.vp.game.units.Wolf;
import com.vp.game.worldelements.Floor;

public class ObstacleWorker implements ChunkWorker {
	
	private final float chunkHeight;	
	private final float chunkWidth;
	
	public ObstacleWorker(float chunkHeight, float chunkWidth) {
		this.chunkHeight = chunkHeight;
		this.chunkWidth = chunkWidth;
	}
	@Override
	public void onChunkSpawn(Chunk chunk) {
		
		for(int i = 0; i < 10; i++){
			float spawnPositionX = ((float) Math.random()) * (chunkWidth-4*Wolf.STANDARD_RADIUS) + chunk.position + 2*Wolf.STANDARD_RADIUS;
			float spawnPositionZ = ((float) Math.random()) * (chunkHeight-2*Wolf.STANDARD_RADIUS) - (chunkHeight/2-Wolf.STANDARD_RADIUS);

			Wolf wolf = Wolf.pool.obtain();
			wolf.setAttributes(spawnPositionX, 0, spawnPositionZ, 1, 0, 20);
			boolean collides = false;
			for(Obstacle w : chunk.obstacles){
				if(wolf.collidesWith(w)){
					collides = true;
					break;
				}
			}
			if(collides){
				Wolf.pool.free(wolf);
			}else{
				if(Obstacle.spatialHashGrid.put(wolf, spawnPositionX, spawnPositionZ))
					chunk.obstacles.add(wolf);
			}
		}
		
	}

	@Override
	public void onChunkClear(Chunk chunk) {
		for(Obstacle obs : chunk.obstacles){
			obs.free();
			Obstacle.spatialHashGrid.remove(obs);
		}
		chunk.obstacles.clear();		
	}
}
