package com.vp.game.units;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.vp.game.worldelements.WorldElement;

public abstract class Unit implements WorldElement {
	public static Array<Unit> unitsInRange;
	public static Array<Unit> units;
	
	public Vector2 position;
	public float positionY;
	public Vector2 direction;
	public float speedY;
	public float speed;
	public String animation;
	public boolean animationChange;
	public float radius;
	public float turnSpeed;
	
	public final ModelInstance modelInstance;
	public final AnimationController animC;
	
	public int idInUnitsInRange;
	public int idInUnits;
	
	public Unit(){
		position = new Vector2();
		direction = new Vector2();
		if(getModel()!=null){
			modelInstance = new ModelInstance(getModel());
		}else{
			modelInstance = null;
		}
		animC = null;
	}
	
	public Unit(float radius){
		this.radius = radius;
		position = new Vector2();
		direction = new Vector2();
		if(getModel()!=null){
			modelInstance = new ModelInstance(getModel());
		}else{
			modelInstance = null;
		}
		animC = null;
	}
	
	public Unit(float radius, String animation){
		this.radius = radius;
		position = new Vector2();
		direction = new Vector2();
		if(getModel()!=null){
			this.animation = new String(animation);
			modelInstance = new ModelInstance(getModel());
			animC = new AnimationController(modelInstance);
			animC.setAnimation(animation, -1);
		}else{
			modelInstance = null;
			animC = null;
		}
	}
	
	public Unit(float positionX, float positionY, float positionZ, float directionX, float directionZ, float speed, float radius){
		this.position = new Vector2(positionX, positionZ);
		this.positionY = positionY;
		this.direction = new Vector2(directionX, directionZ);
		this.speed = speed;
		this.radius= radius;
		if(getModel()!=null){
			modelInstance = new ModelInstance(getModel());
		}else{
			modelInstance = null;
		}
		animC = null;
	}
	
	public Unit(float positionX, float positionY, float positionZ, float directionX, float directionZ, float speed, float radius, String animation){
		this.position = new Vector2(positionX, positionZ);
		this.positionY = positionY;
		this.direction = new Vector2(directionX, directionZ);
		this.speed = speed;
		this.radius= radius;
		if(getModel()!=null){
			this.animation = new String(animation);
			modelInstance = new ModelInstance(getModel());
			animC = new AnimationController(modelInstance);
			animC.setAnimation(this.animation, -1);
		}else{
			modelInstance = null;
			animC = null;
		}
	}
	

	public void setAttributes(float positionX, float positionY, float positionZ, float directionX, float directionZ, float speed, String animation){
		this.position.x = positionX;
		this.position.y = positionZ;
		this.positionY = positionY;
		this.direction.x = directionX;
		this.direction.y = directionZ;
		this.speed = speed;
		this.animation = new String(animation);
		animC.setAnimation(animation, -1);
	}
	
	public void setAttributes(float positionX, float positionY, float positionZ, float directionX, float directionZ, float speed){
		this.position.x = positionX;
		this.position.y = positionZ;
		this.positionY = positionY;
		this.direction.x = directionX;
		this.direction.y = directionZ;
		this.speed = speed;
	}
	
	public void move(float positionX, float positionY){
		this.position.x = positionX;
		this.position.y = positionY;
	}
	
	public float distance(Unit otherUnit){
		return position.dst(otherUnit.position);
	}
	
	public float distance2(Unit otherUnit){
		return position.dst2(otherUnit.position);
	}
	
	public boolean collidesWith(Unit otherUnit){
		float minDist = (radius+otherUnit.radius);
		return ((minDist * minDist) > position.dst2(otherUnit.position));
	}

	public abstract Model getModel();
	
	@Override
	public String toString(){
		return this.getClass().getName() + " : X: " + position.x + ", Y: "+ position.y;
	}

	public void free() {
		units.removeIndex(this.idInUnits);
		if(this.idInUnits < units.size){
			units.get(idInUnits).idInUnits = this.idInUnits;
		}
	}
}
