package com.vp.game.worldelements;

import com.badlogic.gdx.graphics.PerspectiveCamera;

public class Floor implements WorldElement {
	//X-coordinates of the centers of the 3 floor tiles
	public float[] xPositions = new float[3];
	
	//The index of the floor tile in the middle
	public int middleIndex = 1;
	
	//If the camera position is not in between these values, the floor tiles need to be moved
	public float maxCamPosLeft;
	public float maxCamPosRight;
	
	//Width of one floor tile
	public float tileWidth;
	
	//Height of the floor
	public float height;
	
	//True if the floor tiles got shifted in this cycle, can be set to false by Renderer
	public boolean hasChanged;
	
	private final PerspectiveCamera cam;
	
	public Floor(PerspectiveCamera cam, float xPosMiddle, float tileWidth, float height){
		this.cam =cam;
		xPositions[1]=xPosMiddle;
		xPositions[2]=xPosMiddle+tileWidth;
		xPositions[0]=xPosMiddle-tileWidth;
		maxCamPosLeft = xPosMiddle - tileWidth/2;
		maxCamPosRight = xPosMiddle + tileWidth/2;
		this.tileWidth = tileWidth;
		this.height = height;
		hasChanged = true;
	}
	
	//If the camera position is not on the middle floor tile, move one edge tile from
	//to the other edge to put the camera in the middle tile again  
	@Override
	public void update(float delta) {
		if(cam.position.x>maxCamPosRight){
			int leftIndex = (middleIndex==0)?2:middleIndex-1;
			int rightIndex= (middleIndex==2)?0:middleIndex+1;
			xPositions[leftIndex] = xPositions[rightIndex] + tileWidth;
			middleIndex=(middleIndex==2)?0:middleIndex+1;
			maxCamPosLeft = maxCamPosRight;
			maxCamPosRight += tileWidth;
			hasChanged = true;
		}else if(cam.position.x<maxCamPosLeft){
			int leftIndex = (middleIndex==0)?2:middleIndex-1;
			int rightIndex= (middleIndex==2)?0:middleIndex+1;
			xPositions[rightIndex] = xPositions[leftIndex] - tileWidth;
			middleIndex=(middleIndex==0)?2:middleIndex-1;;
			maxCamPosRight = maxCamPosLeft;
			maxCamPosLeft -= tileWidth;	
			hasChanged = true;
		}
	}

}
