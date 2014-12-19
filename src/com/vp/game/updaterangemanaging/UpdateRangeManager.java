package com.vp.game.updaterangemanaging;

import com.badlogic.gdx.utils.Array;
import com.vp.game.units.Obstacle;
import com.vp.game.units.Unit;


public class UpdateRangeManager{
	public float updateRangeXStart;
	public float updateRangeXEnd;

	private final Unit unit;
	private float maxXRightPosition; 
	private float maxXLeftPosition; 

	private final float blockSize;
	private final int rangeSizeInCells;

	
	private int middleGlobalChunkID;
	private int middleXPositionID;
	private int middleYPositionID;
	
	private int maxXLeftGlobalChunkID;
	private int maxXLeftXPositionID;

	
	private int maxXRightGlobalChunkID;
	private int maxXRightXPositionID;

	
	public UpdateRangeManager(int rangeSizeInCells,
			Unit unit) {
		super();
		this.unit = unit;
		int[] IDs = new int[3];
		Obstacle.spatialHashGrid.getIDOfPosition(IDs, unit.position.x, unit.position.y);
		middleGlobalChunkID = IDs[0];
		middleXPositionID = IDs[1];
		middleYPositionID = IDs[2];
		
		maxXRightGlobalChunkID = middleGlobalChunkID + (middleXPositionID + (rangeSizeInCells+1)/2)/Obstacle.spatialHashGrid.chunkXDim;
		maxXRightXPositionID = (middleXPositionID + (rangeSizeInCells+1)/2)%Obstacle.spatialHashGrid.chunkXDim;
		
		if(middleXPositionID - rangeSizeInCells/2 >= 0){
			maxXLeftGlobalChunkID = middleGlobalChunkID;
			maxXLeftXPositionID = (middleXPositionID  - rangeSizeInCells/2);
		}else{
			maxXLeftGlobalChunkID = middleGlobalChunkID + (middleXPositionID - rangeSizeInCells/2)/Obstacle.spatialHashGrid.chunkXDim - 1;
			maxXLeftXPositionID = ((middleXPositionID  - rangeSizeInCells/2)%Obstacle.spatialHashGrid.chunkXDim+Obstacle.spatialHashGrid.chunkXDim)%Obstacle.spatialHashGrid.chunkXDim;
		}		
		
		
		this.blockSize = Obstacle.spatialHashGrid.xDimBlockSize;
		this.maxXLeftPosition = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID);
		if(middleXPositionID == Obstacle.spatialHashGrid.maxXInChunk){
			this.maxXRightPosition = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID+1, 0);
		}else{
			this.maxXRightPosition = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID+1);
		}
		
		this.updateRangeXStart = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXLeftGlobalChunkID, maxXLeftXPositionID);
		this.updateRangeXEnd = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXRightGlobalChunkID, maxXRightXPositionID);
		this.rangeSizeInCells = rangeSizeInCells;
		
		//Add units in range
		for(int i = 0; i < Unit.units.size; i++){
			Unit u = Unit.units.get(i);
			if(u.position.x >= updateRangeXStart && u.position.x < updateRangeXEnd){
				u.idInUnitsInRange = Unit.unitsInRange.size;
				Unit.unitsInRange.add(u);
			}
		}
	}

//TODO safe range manager
	public void update() {
		if(unit.position.x>=maxXRightPosition){
			Array<Obstacle>[] newYColumn;
			newYColumn = Obstacle.spatialHashGrid.getYColumn(maxXRightGlobalChunkID, maxXRightXPositionID);
			if(newYColumn != null){
				for(int i = 0; i < newYColumn.length; i++){
					for(int j = 0; j < newYColumn[i].size; j++){
						Unit toAdd = newYColumn[i].get(j);
						toAdd.idInUnitsInRange = Unit.unitsInRange.size;
						if(Unit.unitsInRange.contains(toAdd, true)){
							System.out.println("duplicate");
						}
						if(toAdd.position.x < updateRangeXStart+blockSize || toAdd.position.x >= updateRangeXEnd+blockSize){
							System.out.println("wrong add");
						}
						
						Unit.unitsInRange.add(toAdd);
					}					
				}
			}
			if(middleXPositionID == Obstacle.spatialHashGrid.maxXInChunk){
				middleGlobalChunkID++;
				middleXPositionID = 0;
			}else{
				middleXPositionID++;
			}
			
			if(maxXLeftXPositionID == Obstacle.spatialHashGrid.maxXInChunk){
				maxXLeftGlobalChunkID++;
				maxXLeftXPositionID = 0;
			}else{
				maxXLeftXPositionID++;
			}
			
			if(maxXRightXPositionID == Obstacle.spatialHashGrid.maxXInChunk){
				maxXRightGlobalChunkID++;
				maxXRightXPositionID = 0;
			}else{
				maxXRightXPositionID++;
			}
			
			this.maxXLeftPosition = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID);
			if(middleXPositionID == Obstacle.spatialHashGrid.maxXInChunk){
				this.maxXRightPosition = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID+1, 0);
			}else{
				this.maxXRightPosition = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID+1);
			}
			
			this.updateRangeXStart = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXLeftGlobalChunkID, maxXLeftXPositionID);
			this.updateRangeXEnd = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXRightGlobalChunkID, maxXRightXPositionID);
		}else if(unit.position.x < maxXLeftPosition){
			if(middleXPositionID == 0){
				middleGlobalChunkID--;
				middleXPositionID = Obstacle.spatialHashGrid.maxXInChunk;
			}else{
				middleXPositionID--;
			}
			
			if(maxXLeftXPositionID == 0){
				maxXLeftGlobalChunkID--;
				maxXLeftXPositionID = Obstacle.spatialHashGrid.maxXInChunk;
			}else{
				maxXLeftXPositionID--;
			}
			
			if(maxXRightXPositionID == 0){
				maxXRightGlobalChunkID--;
				maxXRightXPositionID = Obstacle.spatialHashGrid.maxXInChunk;
			}else{
				maxXRightXPositionID--;
			}
			this.maxXLeftPosition = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID);
			if(middleXPositionID == Obstacle.spatialHashGrid.maxXInChunk){
				this.maxXRightPosition = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID+1, 0);
			}else{
				this.maxXRightPosition = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID+1);
			}
			
			Array<Obstacle>[] newYColumn;
			newYColumn = Obstacle.spatialHashGrid.getYColumn(maxXLeftGlobalChunkID, maxXLeftXPositionID);
			if(newYColumn != null){
				for(int i = 0; i < newYColumn.length; i++){
					for(int j = 0; j < newYColumn[i].size; j++){
						Unit toAdd = newYColumn[i].get(j);
						toAdd.idInUnitsInRange = Unit.unitsInRange.size;
						if(Unit.unitsInRange.contains(toAdd, true)){
							System.out.println("duplicate");
							System.exit(1);
						}
						Unit.unitsInRange.add(toAdd);
					}					
				}
			}
			
			this.updateRangeXStart = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXLeftGlobalChunkID, maxXLeftXPositionID);
			this.updateRangeXEnd = Obstacle.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXRightGlobalChunkID, maxXRightXPositionID);			
		}
	}
	
	public void removeFromRange(Unit unit){
		Unit.unitsInRange.removeIndex(unit.idInUnitsInRange);
		if(unit.idInUnitsInRange < Unit.unitsInRange.size){
			Unit.unitsInRange.get(unit.idInUnitsInRange).idInUnitsInRange = unit.idInUnitsInRange;
		}
		
	}
}
