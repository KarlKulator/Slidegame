package com.vp.game.tools;

import com.badlogic.gdx.utils.Array;
import com.vp.game.gameelements.Chunk;
import com.vp.game.units.Obstacle;
import com.vp.game.worldelements.WorldElement;

public class WrappableSpatialHashGrid {
	//first dimension are the chunks
	private final Array<Obstacle>[][][] grid;
	//The array which contains chunks which then contain obstacles
	private final Array<Chunk> chunkArray;
	
	private final float xBlockSize;
	private final float yBlockSize;
	private final float xChunkSize;
	private final float yChunkSize;
	public final float xDimBlockSize;
	public final int maxXInChunk;
	public final int maxYInChunk;
	public int chunkXDim;
	public int chunkYDim;
	
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

	
	@SuppressWarnings("unchecked")
	public WrappableSpatialHashGrid(float xDimBlockSize, float yDimBlockSize, float xChunkSize, float yChunkSize, int numChunks, float topYPosition) {
		this.chunkXDim = (int)Math.ceil(xChunkSize/xDimBlockSize);
		this.chunkYDim = (int)Math.ceil(yChunkSize/yDimBlockSize);
		assert(chunkXDim>=2);
		assert(chunkYDim>=2);
		grid = (Array<Obstacle>[][][]) new Array[numChunks][chunkXDim][chunkYDim];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {					
				for (int k = 0; k < grid[i][j].length; k++) {
					grid[i][j][k] = new Array<Obstacle>(false,3);
				}
			}
		}
		this.xBlockSize = xChunkSize/chunkXDim;
		this.yBlockSize = yChunkSize/chunkYDim;
		this.xChunkSize = xChunkSize;
		this.yChunkSize = yChunkSize;
		this.xDimBlockSize = xDimBlockSize;
		this.maxXInChunk = chunkXDim-1;
		this.maxYInChunk = chunkYDim-1;
		this.numChunks = numChunks;
		this.rightIndex = numChunks-1;
		this.topYPosition = topYPosition;
		this.globalRightChunkID = numChunks -1;
		this.maxChunkID = numChunks-1;
		chunkArray = new Array<Chunk>(false, numChunks);
		for(int i = 0; i< numChunks; i++){
			chunkArray.add(null);
		}
	}
	
	public void setLeftXPosition(float leftXPosition){
		this.leftXPosition = leftXPosition;
	}
	
	public void wrapRight(){
		leftIndex=(leftIndex==(numChunks-1))?0:leftIndex+1;
		rightIndex=(rightIndex==(numChunks-1))?0:rightIndex+1;
		globalLeftChunkID++;
		globalRightChunkID++;
		leftXPosition=getFirstChunk().position;
	}
	
	public void wrapLeft(){
		rightIndex=(rightIndex==0)?(numChunks-1):rightIndex-1;
		leftIndex=(leftIndex==0)?(numChunks-1):leftIndex-1;
		globalLeftChunkID--;
		globalRightChunkID--;
		leftXPosition=getFirstChunk().position;
	}
	
	public float getLeftXPositionOfBlock(float positionX){
		float xPosToLeftSide = positionX - leftXPosition;
		int chunkID = (int) (xPosToLeftSide/xChunkSize);
		int xPosID = (int) ((xPosToLeftSide -  (chunkID * xChunkSize))/xBlockSize);
		return leftXPosition + chunkID * xChunkSize + xPosID * xBlockSize;
	}
	
	public float getXPositionOfCellWithGlobalID(int globalChunkID, int xPositionID){
			return leftXPosition + (globalChunkID-globalLeftChunkID) * xChunkSize + xPositionID * xBlockSize;
	}
	
	public void getIDOfPosition(int[]IDs, float positionX, float positionY){
		float xPosToLeftSide = positionX - leftXPosition;
		int chunkID = (int) (xPosToLeftSide/xChunkSize);
		int globalChunkID = chunkID + globalLeftChunkID;
		int xPosID = (int) ((xPosToLeftSide -  (chunkID * xChunkSize))/xBlockSize);
		int yPosID = (int) ((positionY - topYPosition)/yBlockSize);
		IDs[0] = globalChunkID;
		IDs[1] = xPosID;
 		IDs[2] = yPosID;
	}
	
	public boolean put(Obstacle obs, float positionX, float positionY){
		float xPosToLeftSide = positionX - leftXPosition;
		int chunkID = (int) (xPosToLeftSide/xChunkSize);
		int xPosID = (int) ((xPosToLeftSide -  (chunkID * xChunkSize))/xBlockSize);
		int yPosID = (int) ((positionY - topYPosition)/yBlockSize);
		return put(obs, chunkID, xPosID, yPosID);
	}
	
	public boolean put(Obstacle obs, int chunkID, int xPosID, int yPosID){
		int relativeChunkID = (chunkID+leftIndex)%numChunks;
		try{
			chunkArray.get(relativeChunkID).add(obs);
			grid[relativeChunkID][xPosID][yPosID].add(obs);
			obs.globalChunkID = chunkID + globalLeftChunkID;
			obs.xPositionID = xPosID;
			obs.yPositionID = yPosID;
			return true;	
		}catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	public boolean move(Obstacle obs, float positionX, float positionY){
			float xPosToLeftSide = positionX - leftXPosition;
			int chunkID = (int) (xPosToLeftSide/xChunkSize);
			int xPosID = (int) ((xPosToLeftSide -  (chunkID * xChunkSize))/xBlockSize);
			int yPosID = (int) ((positionY - topYPosition)/yBlockSize);
			int globalChunkID = obs.globalChunkID; 
			int xPositionID = obs.globalChunkID; 
			int yPositionID = obs.globalChunkID;
			
			if(globalChunkID-globalLeftChunkID!=chunkID){
				remove(obs);
				return put(obs, chunkID, xPosID, yPosID);
			}else if (xPosID!=xPositionID||yPosID!=yPositionID){
				grid[((globalChunkID-globalLeftChunkID)+leftIndex)%numChunks][xPositionID][yPositionID].removeValue(obs, true);	
				int relativeChunkID = (chunkID+leftIndex)%numChunks;
				try{					
					grid[relativeChunkID][xPosID][yPosID].add(obs);
					obs.globalChunkID = chunkID + globalLeftChunkID;
					obs.xPositionID = xPosID;
					obs.yPositionID = yPosID;
					return true;	
				}catch(ArrayIndexOutOfBoundsException e){
					return false;
				}
			}
			return true;
	}
	
	public boolean remove(Obstacle obs){
		int relativeChunkID = ((obs.globalChunkID-globalLeftChunkID)+leftIndex)%numChunks;
		try{
			grid[relativeChunkID][obs.xPositionID][obs.yPositionID].removeValue(obs, true);
			chunkArray.get(relativeChunkID).removeObs(obs);
			return true;
		}catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	public void getNeighboursAndMiddle(Array<Obstacle>[] neighbours, float positionX, float positionY){
		float xPosToLeftSide = positionX - leftXPosition;
		int chunkID = (int) (xPosToLeftSide/xChunkSize);
		int xPositionID = (int) ((xPosToLeftSide -  (chunkID * xChunkSize))/xBlockSize);
		int yPositionID = (int) ((positionY - topYPosition)/yBlockSize);
		getNeighbours(neighbours, chunkID, xPositionID, yPositionID);
		neighbours[8] = get(chunkID, xPositionID, yPositionID);
	}
	
	//empty array
	Array<Obstacle> dummy = new Array<Obstacle>();
	
	public void getNeighbours(Array<Obstacle>[] neighbours, int chunkID, int xPositionID, int yPositionID){
		
		int relativeChunkID = (chunkID+leftIndex)%numChunks;
		
		if(xPositionID == 0){
			if(yPositionID == 0){
				neighbours[0] = dummy;
				neighbours[1] = dummy;
				neighbours[2] = dummy;
				neighbours[3] = grid[relativeChunkID][1][0];
				neighbours[4] = grid[relativeChunkID][1][1];
				neighbours[5] = grid[relativeChunkID][0][1];
				if(chunkID == 0){
					neighbours[7] = dummy;
					neighbours[6] = dummy;
				}else{
					int relativeLeftNeighbourChunkID = (relativeChunkID==0?maxChunkID:(relativeChunkID-1));
					neighbours[7] = grid[relativeLeftNeighbourChunkID][maxXInChunk][0];
					neighbours[6] = grid[relativeLeftNeighbourChunkID][maxXInChunk][1];
				}
			}else if(yPositionID == maxYInChunk){
				neighbours[4] = dummy;
				neighbours[5] = dummy;
				neighbours[6] = dummy;
				neighbours[1] = grid[relativeChunkID][0][maxYInChunk-1];
				neighbours[2] = grid[relativeChunkID][1][maxYInChunk-1];
				neighbours[3] = grid[relativeChunkID][1][maxYInChunk];
				if(chunkID == 0){
					neighbours[7] = dummy;
					neighbours[0] = dummy;
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
					neighbours[0] = dummy;
					neighbours[7] = dummy;
					neighbours[6] = dummy;
				}else{
					int relativeLeftNeighbourChunkID = (relativeChunkID==0?maxChunkID:(relativeChunkID-1));
					neighbours[0] = grid[relativeLeftNeighbourChunkID][maxXInChunk][yPositionID-1];
					neighbours[7] = grid[relativeLeftNeighbourChunkID][maxXInChunk][yPositionID];
					neighbours[6] = grid[relativeLeftNeighbourChunkID][maxXInChunk][yPositionID+1];
				}
			}
		}else if(xPositionID == maxXInChunk){
			if(yPositionID == 0){
				neighbours[0] = dummy;
				neighbours[1] = dummy;
				neighbours[2] = dummy;
				if(chunkID == maxChunkID){
					neighbours[3] = dummy;
					neighbours[4] = dummy;
				}else{
					int relativeRightNeighbourChunkID = (relativeChunkID==maxChunkID?0:(relativeChunkID+1));
					neighbours[3] = grid[relativeRightNeighbourChunkID][0][0];
					neighbours[4] = grid[relativeRightNeighbourChunkID][0][1];
				}
				neighbours[5] = grid[relativeChunkID][maxXInChunk][1];
				neighbours[6] = grid[relativeChunkID][maxXInChunk-1][1];
				neighbours[7] = grid[relativeChunkID][maxXInChunk-1][0];
			}else if(yPositionID == maxYInChunk){
				neighbours[4] = dummy;
				neighbours[5] = dummy;
				neighbours[6] = dummy;
				if(chunkID == maxChunkID){
					neighbours[2] = dummy;
					neighbours[3] = dummy;
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
					neighbours[2] = dummy;
					neighbours[3] = dummy;
					neighbours[4] = dummy;
				}else{
					int relativeRightNeighbourChunkID = (relativeChunkID==maxChunkID?0:(relativeChunkID+1));
					neighbours[2] = grid[relativeRightNeighbourChunkID][0][yPositionID-1];
					neighbours[3] = grid[relativeRightNeighbourChunkID][0][yPositionID];
					neighbours[4] = grid[relativeRightNeighbourChunkID][0][yPositionID+1];
				}
			}
		}else{
			if(yPositionID == 0){
				neighbours[0] = dummy;
				neighbours[1] = dummy;
				neighbours[2] = dummy;
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
				neighbours[4] = dummy;
				neighbours[5] = dummy;
				neighbours[6] = dummy;
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
	
	public Array<Obstacle> get(int chunkID, int xPositionID, int yPositionID){
		try{
			return grid[(chunkID+leftIndex)%numChunks][xPositionID][yPositionID];
		}catch(ArrayIndexOutOfBoundsException e){
			return dummy;
		}
	}
	
	public Array<Obstacle>[] getYColumn(int globalChunkID, int xPositionID){
		try{
			return grid[(globalChunkID- globalLeftChunkID +leftIndex)%numChunks][xPositionID];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	public Chunk getChunk(int index){
		return chunkArray.get((leftIndex+index)%numChunks);
	}
	
	public Chunk getFirstChunk(){
		return chunkArray.get(leftIndex);
	}
	
	public Chunk getLastChunk(){
		return chunkArray.get(rightIndex);
	}
	
	public void setChunk(int index, Chunk element){
		chunkArray.set((leftIndex+index)%numChunks, element);
	}
	
	public int getNumChunk() {
		return numChunks;
	}
}
