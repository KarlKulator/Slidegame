package com.vp.game.collisionmanaging;

import com.badlogic.gdx.utils.Array;
import com.vp.game.units.HashedUnit;
import com.vp.game.units.Item;
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
	Array<HashedUnit>[] neighbours = (Array<HashedUnit>[]) new Array[9];
	
	public boolean checkCollisions(){
		HashedUnit.spatialHashGrid.getNeighboursAndMiddle(neighbours, ninja.position.x,  ninja.position.y);
		for(int i = 0; i < 9; i++){
			for (int j = 0; j < neighbours[i].size; j++) {
				HashedUnit u = neighbours[i].get(j);
				if(ninja.collidesWith(u)){
					if(u instanceof Obstacle){
						if(ninja.obsCollideAble){
							return true;
						}
					}else if (u instanceof Item){
						((Item)u).onCollect();
					}					
				}
			}
		}
		return false;
	}
	
	public boolean checkCollisions(Unit unit){
		HashedUnit.spatialHashGrid.getNeighboursAndMiddle(neighbours, unit.position.x,  unit.position.y);
		for(int i = 0; i < 9; i++){
			for (int j = 0; j < neighbours[i].size; j++) {
				Unit n = neighbours[i].get(j);
				if(n != unit && unit.collidesWith(n)){
					if(n instanceof Obstacle){
						return true;
					}					
				}
			}
		}
		return false;
	}
	

}
