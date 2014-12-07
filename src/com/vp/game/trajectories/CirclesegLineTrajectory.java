package com.vp.game.trajectories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.vp.game.units.Unit;

public class CirclesegLineTrajectory extends Trajectory {
	//Radius of the circle segment
	private float radius;
	//The Vector pointing form the middle of the circle segment to the position on the circle before the update
	private Vector2 vecOnCircleOld = new Vector2();
	//The Vector pointing form the middle of the circle segment to the position on the circle after the update
	private Vector2 vecOnCircleNew = new Vector2();	
	//For clockwise circling (in the camera view of this game) its set to 1 else -1
	public int clockwise;
	//Vector pointing in the direction of the line
	private Vector2 lineVector = new Vector2();
	//Is true if the current position is already on the line
	public boolean onLine; 
	
	public CirclesegLineTrajectory(Unit unit){
		super(unit);
	}
	//Set trajectory without circle segment
	public void setTrajectory(float lineVectorX, float lineVectorZ){		
		this.lineVector.set(lineVectorX, lineVectorZ);
		lineVector.nor();
		unit.direction.x = lineVector.x;
		unit.direction.y = lineVector.y;
		onLine = true;
	}
	
	//Set trajectory with circle segment
	public void setTrajectory(Vector2 center, float radius, boolean clockwise, Vector2 lineVector){
		if(radius == 0){
			setTrajectory(lineVector.x, lineVector.y);
		}else{
			this.clockwise=clockwise?1:-1;
			this.lineVector.set(lineVector.x, lineVector.y);
			this.vecOnCircleOld.set(unit.position.x-center.x, unit.position.y - center.y);
			this.vecOnCircleNew.set(vecOnCircleOld);
			this.radius = radius;
			onLine = false;
		}
	}
	
	//Set trajectory with circle segment
	public void setTrajectory(Vector2 center, float radius, boolean clockwise, float lineVectorX, float lineVectorZ){
		if(radius == 0){
			setTrajectory(lineVectorX, lineVectorZ);
		}else{
			this.clockwise=clockwise?1:-1;
			this.lineVector.set(lineVectorX, lineVectorZ);
			this.vecOnCircleOld.set(unit.position.x-center.x, unit.position.y - center.y);
			this.vecOnCircleNew.set(vecOnCircleOld);
			this.radius = radius;
			onLine = false;
		}
	}
	
	//Set trajectory with circle segment
	public void setTrajectory(float centerX, float centerZ, float radius, boolean clockwise, float lineVectorX, float lineVectorZ){
		if(radius == 0){
			setTrajectory(lineVectorX, lineVectorZ);
		}else{
			this.clockwise=clockwise?1:-1;
			this.lineVector.set(lineVectorX, lineVectorZ);
			this.vecOnCircleOld.set(unit.position.x-centerX, unit.position.y - centerZ);
			this.vecOnCircleNew.set(vecOnCircleOld);
			this.radius = radius;
			onLine = false;
		}
	}
	
	@Override
	public void update(float delta) {
		if(onLine){
			//Move on line
			unit.position.add(unit.direction.x*delta*unit.speed, unit.direction.y*delta*unit.speed);
		}else{
			//Move on circle
			//Save vecOnCircleNew
			vecOnCircleOld.set(vecOnCircleNew);
			//Rotate the vector depending on the speed, radius, delta and rotation direction
			float rotationAngle = delta*clockwise*unit.speed/radius;
			vecOnCircleNew.rotateRad(rotationAngle);
			//Rotate direction vector of the unit
			unit.direction.rotateRad(rotationAngle);
			//Move unit by difference of New and Old (setting it to the new position on the circle)
			unit.position.add(vecOnCircleNew.x-vecOnCircleOld.x, vecOnCircleNew.y-vecOnCircleOld.y);
			//Check if lineVector is already reached
			float lr = (lineVector.y*unit.direction.x-lineVector.x*unit.direction.y)* clockwise;
			if(lr< 0){
				//We will be on the line in the next update
				lineVector.nor();
				unit.direction.x = lineVector.x;
				unit.direction.y = lineVector.y;
				onLine=true;
			}
		}		
	}
}
