package com.vp.game.chunkmanaging;

import com.vp.game.gameelements.Chunk;

public interface ChunkWorker {
	public void onChunkSpawn(Chunk chunk);
	public void onChunkClear(Chunk chunk);
}
