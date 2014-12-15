package com.vp.game.collisionmanaging;

import com.badlogic.gdx.utils.Array;
import com.vp.game.gameelements.Chunk;
import com.vp.game.tools.WrappingArray;
import com.vp.game.units.Ninja;
import com.vp.game.units.Obstacle;
import com.vp.game.units.Unit;

public class CollisionManager {
	//The main character to check collision for
	private final Ninja ninja;
	
	public CollisionManager(Ninja ninja) {
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
	
	public boolean checkCollisions(Unit unit){
		Obstacle.spatialHashGrid.getNeighboursAndMiddle(neighbours, unit.position.x,  unit.position.y);
		for(int i = 0; i < 9; i++){
			for (int j = 0; j < neighbours[i].size; j++) {
				Unit n = neighbours[i].get(j);
				if(n != unit && unit.collidesWith(n)){
					return true;
				}
			}
		}
		return false;
	}
	

}
