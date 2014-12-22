package com.vp.game.units;

import com.vp.game.tools.WrappableSpatialHashGrid;

public abstract class HashedUnit extends Unit {

public static WrappableSpatialHashGrid spatialHashGrid;
	
	//The IDs in the spatialHashGrid;
	public int globalChunkID;
	public int xPositionID;
	public int yPositionID;
	public int chunkArrayID;
	
	public HashedUnit(){
		super();
	}
	
	public HashedUnit(float radius){
		super(radius);		
	}
	
	public HashedUnit(float radius, String animation){
		super(radius, animation);		
	}
	
	public void spawn(float positionX, float positionY){
		this.position.x = positionX;
		this.position.y = positionY;
		idInUnits = units.size;
		units.add(this);
		spatialHashGrid.put(this, positionX, positionY);
	}
	
	public void move(float positionX, float positionY){
		this.position.x = positionX;
		this.position.y = positionY;
			if(!spatialHashGrid.move(this, positionX, positionY)){
				System.err.println("failed move on grid");
				free();
			}
		}
		
	
	public void free(){
		spatialHashGrid.remove(this);
		super.free();
	}

}
