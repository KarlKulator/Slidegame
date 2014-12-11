package com.vp.game.updateRangeManaging;

import com.vp.game.units.Ninja;
import com.vp.game.units.Obstacle;


public class UpdateRangeManager{
	public float updateRangeXStart;
	public float updateRangeXEnd;

	private final Ninja ninja;
	private float maxXRightPosition; 
	private float maxXLeftPosition; 

	private final float blockSize;
	private final int rangeSizeInCells;

	public UpdateRangeManager(int rangeSizeInCells,
			Ninja ninja) {
		super();
		this.ninja = ninja;
		this.maxXLeftPosition = Obstacle.spatialHashGrid.getLeftXPositionOfBlock(ninja.position.x);
		this.blockSize = Obstacle.spatialHashGrid.xDimBlockSize;
		this.maxXRightPosition = maxXLeftPosition + blockSize;
		this.updateRangeXStart = maxXLeftPosition - rangeSizeInCells/2 * blockSize;
		this.updateRangeXEnd = maxXLeftPosition + (rangeSizeInCells+1)/2 * blockSize;
		this.rangeSizeInCells = rangeSizeInCells;
	}

//TODO safe range manager
	public void update() {
		if(ninja.position.x>=maxXRightPosition || ninja.position.x<maxXLeftPosition){
			float newMaxLeft = Obstacle.spatialHashGrid.getLeftXPositionOfBlock(ninja.position.x);
			if(maxXLeftPosition != newMaxLeft){
				maxXLeftPosition = newMaxLeft;
				maxXRightPosition = maxXLeftPosition+blockSize;
				updateRangeXStart = maxXLeftPosition - rangeSizeInCells/2 * blockSize;
				updateRangeXEnd = maxXLeftPosition + (rangeSizeInCells+1)/2 * blockSize;
			}			
		}
	}

}
