package com.vp.game.collisionmanaging;

import com.badlogic.gdx.utils.Array;
import com.vp.game.gameelements.Chunk;
import com.vp.game.tools.WrappingArray;
import com.vp.game.units.Ninja;
import com.vp.game.units.Obstacle;

public class CollisionManager {
	//The Chunks(WrappingArray) where the obstacles are on
	private final WrappingArray<Chunk> chunks;
	
	//The main character to check collision for
	private final Ninja ninja;
	
	public CollisionManager(WrappingArray<Chunk> chunks, Ninja ninja) {
		this.chunks = chunks;
		this.ninja = ninja;
	}
	
	@SuppressWarnings("unchecked")
	Array<Obstacle>[] neighbours = (Array<Obstacle>[]) new Array[9];
	
	public boolean checkCollisions(){
		Obstacle.spatialHashGrid.getNeighboursAndMiddle(neighbours, ninja.position.x,  ninja.position.y);
		for(int i = 0; i < 9; i++){
			for (int j = 0; j < neighbours[i].size; j++) {
				if(ninja.collidesWith(neighbours[i].get(j))){
					return true;
				}
			}
		}
		return false;
	}

}
