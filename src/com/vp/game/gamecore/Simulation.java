package com.vp.game.gamecore;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.vp.game.chunkmanaging.ChunkManager;
import com.vp.game.collisionmanaging.CollisionManager;
import com.vp.game.gameelements.Chunk;
import com.vp.game.obstacleupdating.ObstacleUpdater;
import com.vp.game.tools.WrappableSpatialHashGrid;
import com.vp.game.tools.WrappingArray;
import com.vp.game.units.Ninja;
import com.vp.game.units.Obstacle;
import com.vp.game.units.Unit;
import com.vp.game.units.Wolf;
import com.vp.game.updaterangemanaging.UpdateRangeManager;
import com.vp.game.worldelements.Floor;
import com.vp.game.worldelements.Walls;

public class Simulation extends InputAdapter{

	final public PerspectiveCamera cam;

	final public Ninja ninja;
	
	final public ObstacleUpdater obsUpdater;
	
	final public Floor floor;
	
	final public Walls walls;
	
	final public ChunkManager chunkManager;
	
	final public CollisionManager colManager;
	final public UpdateRangeManager upRManager;
	
	final private Plane plane;

	public Simulation(PerspectiveCamera cam) {
		this.cam=cam;
		this.ninja = new Ninja(0,0,0,1,0,0,100,25,10);
		this.plane = new Plane(new Vector3(0,1.0f,0),0);
		this.floor = new Floor(cam, 0, 1400, 200);
		this.walls = new Walls(-floor.height/2,floor.height/2,ninja);
		
		float hashGridBlockSize = (ninja.radius+Wolf.STANDARD_RADIUS);
		Obstacle.spatialHashGrid = new WrappableSpatialHashGrid(hashGridBlockSize, hashGridBlockSize, 400, floor.height, 4, -floor.height/2);
		Unit.unitsInRange = new Array<Unit>(false, 100);	
		Unit.units = new Array<Unit>(false, 100);
		this.colManager = new CollisionManager(ninja);
		this.chunkManager = new ChunkManager(ninja, this, 400, colManager);	
		this.upRManager = new UpdateRangeManager(20, ninja);
		this.obsUpdater = new ObstacleUpdater(colManager, upRManager, walls);	
	}
	
	public boolean update(float delta) {		
		ninja.update(delta);
		cam.position.x = ninja.position.x;
		upRManager.update();
		obsUpdater.update(delta);
		floor.update(delta);
		walls.update(delta);
		chunkManager.update();
		if(colManager.checkCollisions()){
			return false;
		}
		return true;
	}
	
	Vector3 intersection = new Vector3();
	Vector2 center = new Vector2();
	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
		
		Ray pickRay = cam.getPickRay(x, y);		
		Intersector.intersectRayPlane(pickRay, plane, intersection);
		intersection.y=0;		
		
		float dirX =intersection.x-ninja.position.x;
		float dirZ =intersection.z-ninja.position.y;
		if(dirX!=0 || dirZ!=0){
			if(dirZ*ninja.direction.x-dirX*ninja.direction.y>0){
				//If there is no wall guiding or if wall guiding is clockwise we can turn clockwise,
				if((!ninja.isWallGuided)||ninja.tra.clockwise==1){
					center.set(ninja.direction.x, ninja.direction.y).nor().rotate90(1).scl(ninja.rotateRadius).add(ninja.position.x, ninja.position.y);
					ninja.tra.setTrajectory(center,ninja.rotateRadius,true, dirX,dirZ);
				}
			}else{
				//If there is no wall guiding or if wall guiding is counter-clockwise we can turn counter-clockwise
				if((!ninja.isWallGuided)||ninja.tra.clockwise==-1){					
					center.set(ninja.direction.x, ninja.direction.y).nor().rotate90(-1).scl(ninja.rotateRadius).add(ninja.position.x, ninja.position.y);
					ninja.tra.setTrajectory(center,ninja.rotateRadius,false, dirX,dirZ);
				}
			}				
		}		
		return true; // return true to indicate the event was handled
	}

}
