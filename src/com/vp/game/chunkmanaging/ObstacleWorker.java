package com.vp.game.chunkmanaging;

import com.vp.game.collisionmanaging.CollisionManager;
import com.vp.game.gameelements.Chunk;
import com.vp.game.units.FlyItem;
import com.vp.game.units.GhostItem;
import com.vp.game.units.HashedUnit;
import com.vp.game.units.Item;
import com.vp.game.units.Wolf;

public class ObstacleWorker implements ChunkWorker {
	
	private final CollisionManager colManager;
	
	private final float chunkHeight;	
	private final float chunkWidth;
	
	public ObstacleWorker(float chunkHeight, float chunkWidth, CollisionManager colManager) {
		this.chunkHeight = chunkHeight;
		this.chunkWidth = chunkWidth;
		this.colManager = colManager;
	}
	@Override
	public void onChunkSpawn(Chunk chunk) {
		//System.out.println("spawn");
		for(int i = 0; i < 6; i++){
			float spawnPositionX = ((float) Math.random()) * (chunkWidth-4*Wolf.STANDARD_RADIUS) + chunk.position + 2*Wolf.STANDARD_RADIUS;
			float spawnPositionZ = ((float) Math.random()) * (chunkHeight-2*Wolf.STANDARD_RADIUS) - (chunkHeight/2-Wolf.STANDARD_RADIUS);

			Wolf wolf = Wolf.pool.obtain();
			wolf.setAttributes(spawnPositionX, 0, spawnPositionZ, 1, 0, 50);
			if(colManager.checkCollisions(wolf)){
				Wolf.pool.free(wolf);
			}else{
				wolf.tra.finished = true;
				wolf.spawn(spawnPositionX, spawnPositionZ);				
			}
		}	
		
		//items
		for(int i = 0 ; i < 1; i++){
			float spawnPositionX = ((float) Math.random()) * (chunkWidth-4*Wolf.STANDARD_RADIUS) + chunk.position + 2*Wolf.STANDARD_RADIUS;
			float spawnPositionZ = ((float) Math.random()) * (chunkHeight-2*Wolf.STANDARD_RADIUS) - (chunkHeight/2-Wolf.STANDARD_RADIUS);
			Item item;
			if(Math.random()<0.5){
				item = GhostItem.pool.obtain();
			}else{
				item = FlyItem.pool.obtain();
			}
			item.setAttributes(spawnPositionX, 0, spawnPositionZ, 1, 0, 20);
			item.spawn(item.position.x, item.position.y);
		}
	}

	@Override
	public void onChunkClear(Chunk chunk) {
		while(chunk.hashedUnits.size>0){
			HashedUnit hashedUnit = chunk.hashedUnits.get(0);
			hashedUnit.free();
		}
	}
}
