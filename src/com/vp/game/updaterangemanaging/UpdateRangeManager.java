package com.vp.game.updaterangemanaging;

import com.badlogic.gdx.utils.Array;
import com.vp.game.units.HashedUnit;
import com.vp.game.units.HashedUnit;
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
		HashedUnit.spatialHashGrid.getIDOfPosition(IDs, unit.position.x, unit.position.y);
		middleGlobalChunkID = IDs[0];
		middleXPositionID = IDs[1];
		middleYPositionID = IDs[2];
		
		maxXRightGlobalChunkID = middleGlobalChunkID + (middleXPositionID + (rangeSizeInCells+1)/2)/HashedUnit.spatialHashGrid.chunkXDim;
		maxXRightXPositionID = (middleXPositionID + (rangeSizeInCells+1)/2)%HashedUnit.spatialHashGrid.chunkXDim;
		
		if(middleXPositionID - rangeSizeInCells/2 >= 0){
			maxXLeftGlobalChunkID = middleGlobalChunkID;
			maxXLeftXPositionID = (middleXPositionID  - rangeSizeInCells/2);
		}else{
			maxXLeftGlobalChunkID = middleGlobalChunkID + (middleXPositionID - rangeSizeInCells/2)/HashedUnit.spatialHashGrid.chunkXDim - 1;
			maxXLeftXPositionID = ((middleXPositionID  - rangeSizeInCells/2)%HashedUnit.spatialHashGrid.chunkXDim+HashedUnit.spatialHashGrid.chunkXDim)%HashedUnit.spatialHashGrid.chunkXDim;
		}		
		
		
		this.blockSize = HashedUnit.spatialHashGrid.xDimBlockSize;
		this.maxXLeftPosition = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID);
		if(middleXPositionID == HashedUnit.spatialHashGrid.maxXInChunk){
			this.maxXRightPosition = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID+1, 0);
		}else{
			this.maxXRightPosition = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID+1);
		}
		
		this.updateRangeXStart = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXLeftGlobalChunkID, maxXLeftXPositionID);
		this.updateRangeXEnd = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXRightGlobalChunkID, maxXRightXPositionID);
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
			Array<HashedUnit>[] newYColumn;
			newYColumn = HashedUnit.spatialHashGrid.getYColumn(maxXRightGlobalChunkID, maxXRightXPositionID);
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
			if(middleXPositionID == HashedUnit.spatialHashGrid.maxXInChunk){
				middleGlobalChunkID++;
				middleXPositionID = 0;
			}else{
				middleXPositionID++;
			}
			
			if(maxXLeftXPositionID == HashedUnit.spatialHashGrid.maxXInChunk){
				maxXLeftGlobalChunkID++;
				maxXLeftXPositionID = 0;
			}else{
				maxXLeftXPositionID++;
			}
			
			if(maxXRightXPositionID == HashedUnit.spatialHashGrid.maxXInChunk){
				maxXRightGlobalChunkID++;
				maxXRightXPositionID = 0;
			}else{
				maxXRightXPositionID++;
			}
			
			this.maxXLeftPosition = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID);
			if(middleXPositionID == HashedUnit.spatialHashGrid.maxXInChunk){
				this.maxXRightPosition = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID+1, 0);
			}else{
				this.maxXRightPosition = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID+1);
			}
			
			this.updateRangeXStart = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXLeftGlobalChunkID, maxXLeftXPositionID);
			this.updateRangeXEnd = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXRightGlobalChunkID, maxXRightXPositionID);
		}else if(unit.position.x < maxXLeftPosition){
			if(middleXPositionID == 0){
				middleGlobalChunkID--;
				middleXPositionID = HashedUnit.spatialHashGrid.maxXInChunk;
			}else{
				middleXPositionID--;
			}
			
			if(maxXLeftXPositionID == 0){
				maxXLeftGlobalChunkID--;
				maxXLeftXPositionID = HashedUnit.spatialHashGrid.maxXInChunk;
			}else{
				maxXLeftXPositionID--;
			}
			
			if(maxXRightXPositionID == 0){
				maxXRightGlobalChunkID--;
				maxXRightXPositionID = HashedUnit.spatialHashGrid.maxXInChunk;
			}else{
				maxXRightXPositionID--;
			}
			this.maxXLeftPosition = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID);
			if(middleXPositionID == HashedUnit.spatialHashGrid.maxXInChunk){
				this.maxXRightPosition = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID+1, 0);
			}else{
				this.maxXRightPosition = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(middleGlobalChunkID, middleXPositionID+1);
			}
			
			Array<HashedUnit>[] newYColumn;
			newYColumn = HashedUnit.spatialHashGrid.getYColumn(maxXLeftGlobalChunkID, maxXLeftXPositionID);
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
			
			this.updateRangeXStart = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXLeftGlobalChunkID, maxXLeftXPositionID);
			this.updateRangeXEnd = HashedUnit.spatialHashGrid.getXPositionOfCellWithGlobalID(maxXRightGlobalChunkID, maxXRightXPositionID);			
		}
	}
	
	public void removeFromRange(Unit unit){
		Unit.unitsInRange.removeIndex(unit.idInUnitsInRange);
		if(unit.idInUnitsInRange < Unit.unitsInRange.size){
			Unit.unitsInRange.get(unit.idInUnitsInRange).idInUnitsInRange = unit.idInUnitsInRange;
		}		
	}
}
