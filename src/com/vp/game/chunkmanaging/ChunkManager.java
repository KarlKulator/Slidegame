package com.vp.game.chunkmanaging;

import com.vp.game.collisionmanaging.CollisionManager;
import com.vp.game.gamecore.Simulation;
import com.vp.game.gameelements.Chunk;
import com.vp.game.tools.WrappableSpatialHashGrid;
import com.vp.game.tools.WrappingArray;
import com.vp.game.units.Obstacle;
import com.vp.game.units.Unit;
import com.vp.game.units.Wolf;

public class ChunkManager {
	
	//Minimum Position of the unit before wrapping the tiles
	private float minUnitPos;
	//Maximum Position of the unit before wrapping the tiles
	private float maxUnitPos;
	//The unit which controls the wrapping
	private final Unit unit;
	//The WrappableSpatialHashGGrid to be managed
	private final WrappableSpatialHashGrid chunks;
	
	//The ObstacleManager to be informed
	private final ObstacleWorker obsWorker;
	
	//The number of chunks to be managed
	private final int chunkAmount;
	//The width of the chunks
	private final float chunkWidth;
	
	public float getChunkWidth() {
		return chunkWidth;
	}

	public ChunkManager(Unit unit, Simulation sim,
			 float chunkWidth, CollisionManager colManager) {		
		this.unit = unit;
		this.chunks = Obstacle.spatialHashGrid;
		this.chunkAmount = chunks.getNumChunk();
		this.chunkWidth = chunkWidth;
		this.minUnitPos = 0;
		this.maxUnitPos = 2 * chunkWidth;
		this.obsWorker = new ObstacleWorker(sim.floor.height, chunkWidth, colManager);
		float currentPos = 0;
		for(int i = (chunkAmount-1)/2; i < chunkAmount; i++){
			Chunk addChunk = new Chunk(currentPos, chunkWidth);
			chunks.setChunk(i, addChunk);
			currentPos += chunkWidth;
		}
		currentPos = -chunkWidth;
		for(int i = (chunkAmount-1)/2-1; i >= 0; i--){
			chunks.setChunk(i, new Chunk(currentPos, chunkWidth));
			currentPos -= chunkWidth;
		}	
		Obstacle.spatialHashGrid.setLeftXPosition(chunks.getFirstChunk().position);
	}
	
	public void initChunks(){
		for(int i = (chunkAmount-1)/2; i < chunkAmount; i++){
			spawn(chunks.getChunk(i));
		}
	}
	
	public void update() {
		if(unit.position.x > maxUnitPos){
			Chunk wrappedChunk = chunks.getFirstChunk();
			clear(wrappedChunk);
			wrappedChunk.position = chunks.getLastChunk().position + chunkWidth;
			chunks.wrapRight();	
			spawn(wrappedChunk);
			minUnitPos += chunkWidth;
			maxUnitPos += chunkWidth;
		}else if(unit.position.x < minUnitPos){
			Chunk wrappedChunk = chunks.getLastChunk();
			clear(wrappedChunk);
			wrappedChunk.position = chunks.getFirstChunk().position - chunkWidth;
			chunks.wrapLeft();	
			spawn(wrappedChunk);
			minUnitPos -= chunkWidth;
			maxUnitPos -= chunkWidth;
		}
	}

	private void spawn(Chunk chunkToSpawnON) {
		obsWorker.onChunkSpawn(chunkToSpawnON);
	}

	private void clear(Chunk chunkToClear) {
		obsWorker.onChunkClear(chunkToClear);
	}
	
	//Returns the XPosition of the chunk on the left
	public float getXPosition(){
		return chunks.getFirstChunk().position;
	}
	
}
