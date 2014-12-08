package com.vp.game.gameelements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.vp.game.units.Obstacle;

public class Chunk{
	
	public final List<Obstacle> obstacles;
	public float position;
	public float width;
	
	public Chunk(float position, float width) {
		this.obstacles = new LinkedList<Obstacle>();
		this.position = position;
		this.width = width;
	}
	
	public Chunk(float width) {
		this.obstacles = new LinkedList<Obstacle>();
		this.width = width;
	}
}
