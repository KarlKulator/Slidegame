package com.vp.game.tools;

import com.vp.game.units.Obstacle;

public class WrappableSpatialHashGrid {
	//first dimension are the chunks
	private final Obstacle[][][] grid;
	private final float xBlockSize;
	private final float yBlockSize;
	private final float xChunkSize;
	private final float yChunkSize;
	private final int maxXInChunk;
	private final int maxYInChunk;
	
	
	//The index in array of the chunk on the left
	private int leftIndex;
	//The index in array of the chunk on the right
	private int rightIndex;
	//The number of chunks to be managed
	private final int numChunks;
	
	private final int maxChunkID;
	
	//The x position of the left side of the chunk on the left
	private float leftXPosition;
	//The y position of the top side of the chunks
	private float topYPosition;
	
	//Global ID of the current left chunk
	private int globalLeftChunkID;
	//Global ID of the current right chunk
	private int globalRightChunkID;
	
	public WrappableSpatialHashGrid(float xDimBlockSize, float yDimBlockSize, float xChunkSize, float yChunkSize, int numChunks, float leftXPosition, float topYPosition) {
		int chunkXDim = (int)Math.ceil(xChunkSize/xDimBlockSize);
		int chunkYDim = (int)Math.ceil(yChunkSize/yDimBlockSize);
		assert(chunkXDim>=2);
		assert(chunkYDim>=2);
		grid = new Obstacle[numChunks][chunkXDim][chunkYDim];
		this.xBlockSize = xChunkSize/chunkXDim;
		this.yBlockSize = yChunkSize/chunkYDim;
		this.xChunkSize = xChunkSize;
		this.yChunkSize = yChunkSize;
		this.maxXInChunk = chunkXDim-1;
		this.maxYInChunk = chunkYDim-1;
		this.numChunks = numChunks;
		this.rightIndex = numChunks-1;
		this.leftXPosition = leftXPosition;
		this.topYPosition = topYPosition;
		this.globalRightChunkID = numChunks -1;
		this.maxChunkID = numChunks-1;
	}
	
	public void wrapRight(){
		leftIndex=(leftIndex==(numChunks-1))?0:leftIndex+1;
		rightIndex=(rightIndex==(numChunks-1))?0:rightIndex+1;
		globalLeftChunkID++;
		globalRightChunkID++;
	}
	
	public void wrapLeft(){
		rightIndex=(rightIndex==0)?(numChunks-1):rightIndex-1;
		leftIndex=(leftIndex==0)?(numChunks-1):leftIndex-1;
		globalLeftChunkID--;
		globalRightChunkID--;
	}
	
	public boolean put(Obstacle obs, float positionX, float positionY){
		float xPosToLeftSide = positionX - leftXPosition;
		int chunkID = (int) (xPosToLeftSide/xChunkSize);
		int relativeChunkID = (chunkID+leftIndex)%numChunks;
		int xPosID = (int) ((xPosToLeftSide -  (chunkID * xChunkSize))/xBlockSize);
		int yPosID = (int) ((positionY - topYPosition)/yBlockSize);
		try{
			if(grid[relativeChunkID][xPosID][yPosID] == null){
				grid[relativeChunkID][xPosID][yPosID] = obs;
				obs.globalChunkID = chunkID + globalLeftChunkID;
				obs.xPositionID = xPosID;
				obs.yPositionID = yPosID;
				return true;
			}else{
				//collision
				return false;
			}	
		}catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	public boolean move(Obstacle obs, float positionX, float positionY, int globalChunkID, int xPositionID, int yPositionID){
			grid[((globalChunkID-globalLeftChunkID)+leftIndex)%numChunks][xPositionID][yPositionID] = null;
			return put(obs, positionX, positionY);
	}
	
	public void remove(Obstacle obs){
		grid[((obs.globalChunkID-globalLeftChunkID)+leftIndex)%numChunks][obs.xPositionID][obs.yPositionID] = null;
	}
	public void getNeighboursAndMiddle(Obstacle[] neighbours, float positionX, float positionY){
		float xPosToLeftSide = positionX - leftXPosition;
		int chunkID = (int) (xPosToLeftSide/xChunkSize);
		int xPositionID = (int) ((xPosToLeftSide -  (chunkID * xChunkSize))/xBlockSize);
		int yPositionID = (int) ((positionY - topYPosition)/yBlockSize);
		getNeighbours(neighbours, chunkID, xPositionID, yPositionID);
		neighbours[8] = get(chunkID, xPositionID, yPositionID);
	}
	public void getNeighbours(Obstacle[] neighbours, int chunkID, int xPositionID, int yPositionID){
		
		int relativeChunkID = (chunkID+leftIndex)%numChunks;
		
		if(xPositionID == 0){
			if(yPositionID == 0){
				neighbours[0] = null;
				neighbours[1] = null;
				neighbours[2] = null;
				neighbours[3] = grid[relativeChunkID][1][0];
				neighbours[4] = grid[relativeChunkID][1][1];
				neighbours[5] = grid[relativeChunkID][0][1];
				if(chunkID == 0){
					neighbours[7] = null;
					neighbours[6] = null;
				}else{
					int relativeLeftNeighbourChunkID = (relativeChunkID==0?maxChunkID:(relativeChunkID-1));
					neighbours[7] = grid[relativeLeftNeighbourChunkID][maxXInChunk][0];
					neighbours[6] = grid[relativeLeftNeighbourChunkID][maxXInChunk][1];
				}
			}else if(yPositionID == maxYInChunk){
				neighbours[4] = null;
				neighbours[5] = null;
				neighbours[6] = null;
				neighbours[1] = grid[relativeChunkID][0][maxYInChunk-1];
				neighbours[2] = grid[relativeChunkID][1][maxYInChunk-1];
				neighbours[3] = grid[relativeChunkID][1][maxYInChunk];
				if(chunkID == 0){
					neighbours[7] = null;
					neighbours[0] = null;
				}else{
					int relativeLeftNeighbourChunkID = (relativeChunkID==0?maxChunkID:(relativeChunkID-1));
					neighbours[7] = grid[relativeLeftNeighbourChunkID][maxXInChunk][maxYInChunk];
					neighbours[0] = grid[relativeLeftNeighbourChunkID][maxXInChunk][maxYInChunk-1];
				}
			}else{
				neighbours[1] = grid[relativeChunkID][0][yPositionID-1];
				neighbours[2] = grid[relativeChunkID][1][yPositionID-1];
				neighbours[3] = grid[relativeChunkID][1][yPositionID];
				neighbours[4] = grid[relativeChunkID][1][yPositionID+1];
				neighbours[5] = grid[relativeChunkID][0][yPositionID+1];
				if(chunkID == 0){
					neighbours[0] = null;
					neighbours[7] = null;
					neighbours[6] = null;
				}else{
					int relativeLeftNeighbourChunkID = (relativeChunkID==0?maxChunkID:(relativeChunkID-1));
					neighbours[0] = grid[relativeLeftNeighbourChunkID][maxXInChunk][yPositionID-1];
					neighbours[7] = grid[relativeLeftNeighbourChunkID][maxXInChunk][yPositionID];
					neighbours[6] = grid[relativeLeftNeighbourChunkID][maxXInChunk][yPositionID+1];
				}
			}
		}else if(xPositionID == maxXInChunk){
			if(yPositionID == 0){
				neighbours[0] = null;
				neighbours[1] = null;
				neighbours[2] = null;
				if(chunkID == maxChunkID){
					neighbours[3] = null;
					neighbours[4] = null;
				}else{
					int relativeRightNeighbourChunkID = (relativeChunkID==maxChunkID?0:(relativeChunkID+1));
					neighbours[3] = grid[relativeRightNeighbourChunkID][0][0];
					neighbours[4] = grid[relativeRightNeighbourChunkID][0][1];
				}
				neighbours[5] = grid[relativeChunkID][maxXInChunk][1];
				neighbours[6] = grid[relativeChunkID][maxXInChunk-1][1];
				neighbours[7] = grid[relativeChunkID][maxXInChunk-1][0];
			}else if(yPositionID == maxYInChunk){
				neighbours[4] = null;
				neighbours[5] = null;
				neighbours[6] = null;
				if(chunkID == maxChunkID){
					neighbours[2] = null;
					neighbours[3] = null;
				}else{
					int relativeRightNeighbourChunkID = (relativeChunkID==maxChunkID?0:(relativeChunkID+1));
					neighbours[2] = grid[relativeRightNeighbourChunkID][0][maxYInChunk-1];
					neighbours[3] = grid[relativeRightNeighbourChunkID][0][maxYInChunk];
				}
				neighbours[7] = grid[relativeChunkID][maxXInChunk-1][0];
				neighbours[0] = grid[relativeChunkID][maxXInChunk-1][1];
				neighbours[1] = grid[relativeChunkID][maxXInChunk][0];
			}else{
				neighbours[0] = grid[relativeChunkID][maxXInChunk-1][yPositionID-1];
				neighbours[1] = grid[relativeChunkID][maxXInChunk][yPositionID];
				neighbours[5] = grid[relativeChunkID][maxXInChunk][yPositionID+1];
				neighbours[6] = grid[relativeChunkID][maxXInChunk-1][yPositionID+1];
				neighbours[7] = grid[relativeChunkID][maxXInChunk-1][yPositionID];
				if(chunkID == 0){
					neighbours[2] = null;
					neighbours[3] = null;
					neighbours[4] = null;
				}else{
					int relativeRightNeighbourChunkID = (relativeChunkID==maxChunkID?0:(relativeChunkID+1));
					neighbours[2] = grid[relativeRightNeighbourChunkID][0][yPositionID-1];
					neighbours[3] = grid[relativeRightNeighbourChunkID][0][yPositionID];
					neighbours[4] = grid[relativeRightNeighbourChunkID][0][yPositionID+1];
				}
			}
		}else{
			if(yPositionID == 0){
				neighbours[0] = null;
				neighbours[1] = null;
				neighbours[2] = null;
				neighbours[3] = grid[relativeChunkID][xPositionID+1][yPositionID];
				neighbours[4] = grid[relativeChunkID][xPositionID+1][yPositionID+1];
				neighbours[5] = grid[relativeChunkID][xPositionID][yPositionID+1];
				neighbours[6] = grid[relativeChunkID][xPositionID-1][yPositionID+1];
				neighbours[7] = grid[relativeChunkID][xPositionID-1][yPositionID];
			}else if(yPositionID == maxYInChunk){
				neighbours[0] = grid[relativeChunkID][xPositionID-1][yPositionID-1];
				neighbours[1] = grid[relativeChunkID][xPositionID][yPositionID-1];
				neighbours[2] = grid[relativeChunkID][xPositionID+1][yPositionID-1];
				neighbours[3] = grid[relativeChunkID][xPositionID+1][yPositionID];
				neighbours[4] = null;
				neighbours[5] = null;
				neighbours[6] = null;
				neighbours[7] = grid[relativeChunkID][xPositionID-1][yPositionID];
			}else{
				neighbours[0] = grid[relativeChunkID][xPositionID-1][yPositionID-1];
				neighbours[1] = grid[relativeChunkID][xPositionID][yPositionID-1];
				neighbours[2] = grid[relativeChunkID][xPositionID+1][yPositionID-1];
				neighbours[3] = grid[relativeChunkID][xPositionID+1][yPositionID];
				neighbours[4] = grid[relativeChunkID][xPositionID+1][yPositionID+1];
				neighbours[5] = grid[relativeChunkID][xPositionID][yPositionID+1];
				neighbours[6] = grid[relativeChunkID][xPositionID-1][yPositionID+1];
				neighbours[7] = grid[relativeChunkID][xPositionID-1][yPositionID];
			}
		}		
	}
	
	public Obstacle get(int chunkID, int xPositionID, int yPositionID){
		try{
			return grid[(chunkID+leftIndex)%numChunks][xPositionID][yPositionID];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
}
