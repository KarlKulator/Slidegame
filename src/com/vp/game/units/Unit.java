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
	private static Model model;
	
	public int idInUnitsInRange;
	public int idInUnits;
	
	public Unit(){
		position = new Vector2();
		direction = new Vector2();
		if(model!=null){
			modelInstance = new ModelInstance(model);
			animC = new AnimationController(modelInstance);
			animC.setAnimation("Take 001", -1);
		}else{
			modelInstance = null;
			animC = null;
		}
	}
	
	public Unit(float radius){
		this.radius = radius;
		position = new Vector2();
		direction = new Vector2();
		if(model!=null){
			modelInstance = new ModelInstance(model);
			animC = new AnimationController(modelInstance);
			animC.setAnimation("Take 001", -1);
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
		if(model!=null){
			modelInstance = new ModelInstance(model);
			animC = new AnimationController(modelInstance);
			animC.setAnimation("Take 001", -1);
		}else{
			modelInstance = null;
			animC = null;
		}
	}
	
	public Unit(float positionX, float positionY, float positionZ, float directionX, float directionZ, float speed, float radius, String animation){
		this.position = new Vector2(positionX, positionZ);
		this.positionY = positionY;
		this.direction = new Vector2(positionX, positionZ);
		this.speed = speed;
		this.radius= radius;
		animation = new String(animation);
		if(model!=null){
			modelInstance = new ModelInstance(model);
			animC = new AnimationController(modelInstance);
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
		animation = new String(animation);
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
	
	public static void setModel(Model setModel){
		model = setModel;
	}	
}
