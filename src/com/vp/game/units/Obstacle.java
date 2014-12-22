package com.vp.game.units;



public abstract class Obstacle extends HashedUnit{
	
	public Obstacle(){
		super();
	}
	
	public Obstacle(float radius, String animation){
		super(radius, animation);		
	}
	
	public void free(){
		super.free();
	}
		
}
