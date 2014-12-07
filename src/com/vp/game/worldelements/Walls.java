package com.vp.game.worldelements;

import com.vp.game.units.Ninja;


public class Walls implements WorldElement {
	//Position of the top wall in z-coordinates
	public float topWallPosition;
	//Position of the bottom wall in z-coordinates
	public float bottomWallPosition;
	//The ninja can not get further top in z-coordinates
	public float maxTopPosition;
	//The ninja can not get further bottom in z-coordinates
	public float maxBottomPosition;
	//The ninja should not get further top in z-coordinates
	private float maxTryTopPosition;
	//The ninja should not get further bottom in z-coordinates
	private float maxTryBottomPosition;
	//The position of the bottom side of the critical area on the top wall where we might have to guide in z-coordinates
	private float topWallCrit;
	//The position of the top side of the critical area on the bottom wall where we might have to guide in z-coordinates
	private float bottomWallCrit;
	//The ninja who should not crash in the wall
	private Ninja ninja;
	
	public Walls(float topWallPosition, float bottomWallPosition, Ninja ninja){
		this.topWallPosition=topWallPosition;
		this.bottomWallPosition=bottomWallPosition;
		this.maxTopPosition=topWallPosition+ninja.radius;
		this.maxBottomPosition=bottomWallPosition-ninja.radius;
		this.maxTryTopPosition=maxTopPosition+1;
		this.maxTryBottomPosition=maxBottomPosition-1;
		this.topWallCrit= this.maxTryTopPosition + ninja.rotateRadius;
		this.bottomWallCrit=  this.maxTryBottomPosition - ninja.rotateRadius;
		this.ninja = ninja;
		this.ninja.isWallGuided = false;
	}
	@Override
	public void update(float delta) {
		//Check if we are already way over the wall and if yes do a hard reset (safety check)
		if(ninja.position.y<maxTopPosition){
			ninja.position.y = maxTopPosition;
			if(ninja.direction.x>0){
				ninja.direction.x = 1;
				ninja.direction.y = 0;
			}else{
				ninja.direction.x = -1;
				ninja.direction.y = 0;
			}
			ninja.isWallGuided= true;
			ninja.isTopWallGuided = true;
		}else if (ninja.position.y>maxBottomPosition){
			ninja.position.y = maxBottomPosition;
			ninja.direction.y=0;
			if(ninja.direction.x>0){
				ninja.direction.x = 1;
				ninja.direction.y = 0;
			}else{
				ninja.direction.x = -1;
				ninja.direction.y = 0;
			}
			ninja.isWallGuided= true;
			ninja.isBottomWallGuided = true;
		}
		
		//Check if we need to end Wall Guiding
		if(ninja.isWallGuided){	
			//check if we are facing away from the wall or are already out of critical area
			if(ninja.isTopWallGuided&&(ninja.direction.y>0||ninja.position.y>topWallCrit)){
				ninja.isWallGuided = false;
				ninja.isTopWallGuided = false;
			}else if(ninja.isBottomWallGuided&&(ninja.direction.y<0||ninja.position.y<bottomWallCrit)){
				ninja.isWallGuided = false;
				ninja.isBottomWallGuided = false;
			}
		}else{
		//Check for guidance

			//Check if we are near one wall, so that we might have to guide the ninja away from the wall
			//The area is chosen so that even if the ninja drifts perpendicularly onto the wall we can still guide him away,
			//but we do not have to calculate much if he is far away from the wall
			if(ninja.position.y<topWallCrit&&ninja.direction.y<0){
				
				//We are in the critical area on the top wall and facing towards it
				//Now check if we really need to guide the ninja
				if(ninja.direction.x>0){
					//The distance between the middle point of the circle when immediately turning right and the maxTopPosition
					float dMiddlePointToWall = (ninja.position.y+ninja.direction.x*ninja.rotateRadius) - maxTryTopPosition;
					//If the following expression is true we need to turn right
					if(dMiddlePointToWall<ninja.rotateRadius){
						//We really need to turn right (clockwise) if we are not already turning right
						if((ninja.tra.clockwise==-1) || ninja.tra.onLine ){
							ninja.isWallGuided = true;
							ninja.isTopWallGuided = true;
							//The radius used for turning is dMiddlePointToWall if it is not negative and it is an approximation for the needed rotate radius (calculating the real needed rotate radius is numerically instable, dMiddlePointToWall is always bigger)
							ninja.tra.setTrajectory(ninja.position.x-ninja.direction.y*ninja.rotateRadius, ninja.position.y+ninja.direction.x*ninja.rotateRadius, dMiddlePointToWall < 0 ? ninja.rotateRadius : dMiddlePointToWall, true, 1, 0);
						}						
					}
				}else{
					float dMiddlePointToWall = (ninja.position.y-ninja.direction.x*ninja.rotateRadius) - maxTryTopPosition;
					//If the following expression is true we need to turn left
					if(dMiddlePointToWall<ninja.rotateRadius){
						//We really need to turn left (counter-clockwise) if we are not already turning left
						if((ninja.tra.clockwise==1) || ninja.tra.onLine ){
							ninja.isWallGuided = true;
							ninja.isTopWallGuided = true;
							ninja.tra.setTrajectory(ninja.position.x+ninja.direction.y*ninja.rotateRadius, ninja.position.y-ninja.direction.x*ninja.rotateRadius, dMiddlePointToWall < 0 ? ninja.rotateRadius : dMiddlePointToWall, false, -1, 0);
						}
					}
				}
			}else if(ninja.position.y>bottomWallCrit&&ninja.direction.y>0){
				//We are in the critical area on the bottom wall and facing towards it
				//Now check if we really need to guide the ninj
				if(ninja.direction.x>0){
					float dMiddlePointToWall = maxTryBottomPosition - (ninja.position.y-ninja.direction.x*ninja.rotateRadius) ;
					//If the following expression is true (some 2D Geometry) we need to turn left
					if(dMiddlePointToWall<ninja.rotateRadius){
						//We really need to turn left (counter-clockwise) if we are not already turning left					
						if((ninja.tra.clockwise==1) || ninja.tra.onLine ){
							ninja.isWallGuided = true;
							ninja.isBottomWallGuided = true;
							ninja.tra.setTrajectory(ninja.position.x+ninja.direction.y*ninja.rotateRadius, ninja.position.y-ninja.direction.x*ninja.rotateRadius, dMiddlePointToWall < 0 ? ninja.rotateRadius : dMiddlePointToWall, false, 1, 0);
						}
					}
				}else{
					float dMiddlePointToWall = maxTryBottomPosition - (ninja.position.y+ninja.direction.x*ninja.rotateRadius) ;
					//If the following expression is true (some 2D Geometry) we need to turn right
					if(dMiddlePointToWall<ninja.rotateRadius){
						//We really need to turn right (clockwise) if we are not already turning right
						if((ninja.tra.clockwise==-1) || ninja.tra.onLine ){
							ninja.isWallGuided = true;
							ninja.isBottomWallGuided = true;
							ninja.tra.setTrajectory(ninja.position.x-ninja.direction.y*ninja.rotateRadius, ninja.position.y+ninja.direction.x*ninja.rotateRadius, dMiddlePointToWall < 0 ? ninja.rotateRadius : dMiddlePointToWall, true, -1, 0);
						}
					}
				}
			}
		}
	}

}
