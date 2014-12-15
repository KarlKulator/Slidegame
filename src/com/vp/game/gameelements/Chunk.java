package com.vp.game.gameelements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.vp.game.units.Obstacle;

public class Chunk{
	
	public final Array<Obstacle> obstacles;
	public float position;
	public float width;
	
	public Chunk(float position, float width) {
		this(width);	
		this.position = position;			
	}
	
	public Chunk(float width) {
		this.obstacles = new Array<Obstacle>(false, 40);
		this.width = width;
	}
	
	public void removeObs(Obstacle obs){
		int id = obs.chunkArrayID;
		obstacles.removeIndex(id);
		if( id < obstacles.size){
			obstacles.get(id).chunkArrayID = id;
		}
	}

	public void add(Obstacle obs) {
		obs.chunkArrayID = obstacles.size;
		obstacles.add(obs);		
	}
}
