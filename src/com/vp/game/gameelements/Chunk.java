package com.vp.game.gameelements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.vp.game.units.HashedUnit;
import com.vp.game.units.Obstacle;

public class Chunk{
	
	public final Array<HashedUnit> hashedUnits;
	public float position;
	public float width;
	
	public Chunk(float position, float width) {
		this(width);	
		this.position = position;			
	}
	
	public Chunk(float width) {
		this.hashedUnits = new Array<HashedUnit>(false, 40);
		this.width = width;
	}
	
	public void removeHashedUnit(HashedUnit hashedUnit){
		int id = hashedUnit.chunkArrayID;
		hashedUnits.removeIndex(id);
		if( id < hashedUnits.size){
			hashedUnits.get(id).chunkArrayID = id;
		}
	}

	public void add(HashedUnit hashedUnit) {
		hashedUnit.chunkArrayID = hashedUnits.size;
		hashedUnits.add(hashedUnit);		
	}
}
