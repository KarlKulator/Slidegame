package com.vp.game.units;

import com.badlogic.gdx.utils.Array;
import com.vp.game.tools.ListElement;
import com.vp.game.tools.WrappableSpatialHashGrid;


public abstract class Obstacle extends Unit{
	public static WrappableSpatialHashGrid spatialHashGrid;
	
	//The IDs in the spatialHashGrid;
	public int globalChunkID;
	public int xPositionID;
	public int yPositionID;
	public int chunkArrayID;

	
	public Obstacle(float positionX, float positionY, float positionZ,
			float directionX, float directionY, float directionZ, float speed,
			float radius) {
		super(positionX, positionY, positionZ, directionX,  directionZ,
				speed, radius);
	}
	
	public Obstacle(){
		super();
	}
	
	public Obstacle(float radius){
		super(radius);		
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
				System.out.println(false);
			}
		}
		
	
	public abstract void free();
	
	
}
