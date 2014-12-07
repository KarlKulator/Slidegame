package com.vp.game.chunkmanaging;

import com.vp.game.collisionmanaging.CollisionManager;
import com.vp.game.gamecore.Simulation;
import com.vp.game.gameelements.Chunk;
import com.vp.game.tools.WrappingArray;
import com.vp.game.units.Unit;

public class ChunkManager {
	
	//Minimum Position of the unit before wrapping the tiles
	private float minUnitPos;
	//Maximum Position of the unit before wrapping the tiles
	private float maxUnitPos;
	//The unit which controls the wrapping
	private final Unit unit;
	//The WrappingArray to be managed
	private final WrappingArray<Chunk> chunks;
	
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
			WrappingArray<Chunk> chunks, float chunkWidth) {
		this.unit = unit;
		this.chunks = chunks;
		this.chunkAmount = chunks.getSize();
		this.chunkWidth = chunkWidth;
		this.minUnitPos = 0;
		this.maxUnitPos = 2 * chunkWidth;
		this.obsWorker = new ObstacleWorker(sim.floor.height, chunkWidth);
		float currentPos = 0;
		for(int i = (chunkAmount-1)/2; i < chunkAmount; i++){
			Chunk addChunk = new Chunk(currentPos, chunkWidth);
			chunks.set(i, addChunk);
			currentPos += chunkWidth;
		}
		currentPos = -chunkWidth;
		for(int i = (chunkAmount-1)/2-1; i >= 0; i--){
			chunks.set(i, new Chunk(currentPos, chunkWidth));
			currentPos -= chunkWidth;
		}	
		
	}
	
	public void initChunks(){
		for(int i = (chunkAmount-1)/2; i < chunkAmount; i++){
			spawn(chunks.get(i));
		}
	}
	
	public void update() {
		if(unit.position.x > maxUnitPos){
			Chunk wrappedChunk = chunks.getFirst();
			clear(wrappedChunk);
			wrappedChunk.position = chunks.getLast().position + chunkWidth;
			chunks.wrapRight();	
			spawn(wrappedChunk);
			minUnitPos += chunkWidth;
			maxUnitPos += chunkWidth;
		}else if(unit.position.x < minUnitPos){
			Chunk wrappedChunk = chunks.getLast();
			clear(wrappedChunk);
			wrappedChunk.position = chunks.getFirst().position - chunkWidth;
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
		return chunks.getFirst().position;
	}
	
}
