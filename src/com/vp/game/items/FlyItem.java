package com.vp.game.items;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Pool;
import com.vp.game.units.Ninja;
import com.vp.game.units.Unit;

public class FlyItem extends Item {
	
	public final static float STANDARD_RADIUS = 6; 
	public final static float STANDARD_DURATION = 5; 

	private final static float triggerHeight = 30;
	private boolean preTrigger;
	
	private static Model model;
	public final static Pool<FlyItem> pool = new Pool<FlyItem>(){
		@Override
		protected FlyItem newObject(){
			System.out.println("new FlyItem, still free:" + pool.getFree());
			return new FlyItem();
		}
	};

	
	public FlyItem() {
		this(STANDARD_RADIUS);
	}

	public FlyItem(float radius) {
		super(radius);
		hasEpilog = true;
		this.duration = STANDARD_DURATION;
		itemID=1;
	}

	public FlyItem(float radius, String animation) {
		super(radius, animation);
		hasEpilog = true;
		this.duration = STANDARD_DURATION;
		itemID=1;
	}

	@Override
	public void updateDuringActive(float delta) {
		if(preTrigger){
			ninja.positionY += delta * ninja.speedY;
			if(ninja.positionY >= triggerHeight){
				ninja.speed = 400;
				ninja.positionY = triggerHeight;
				ninja.collideAble = false;	
				preTrigger = false;
			}
		}		
	}
	
	//returns true if epiloge finished
	@Override
	public boolean updateDuringEpilog(float delta) {
		ninja.positionY -= delta * ninja.speedY;
		if(ninja.positionY <= 0){
			ninja.positionY = 0;
			return true;
		}
		return false;
	}
	
	
	@Override
	public void free() {
		super.free();	
		pool.free(this);
	}
	
	@Override
	public void onCollect(Ninja ninja) {
		Item item;
		if((item = ninja.activeItems.getItemByID(itemID)) == null){
			this.ninja = ninja;
			ninja.activeItems.addItem(this);
			ninja.speedY = 60;
			preTrigger = true;
			inEpilog = false;
			durationLeft = duration;
		}else{
			ninja.activeItems.refreshDuration(item);
		}
		Unit.unitsInRange.removeIndex(this.idInUnitsInRange);
		if(this.idInUnitsInRange < Unit.unitsInRange.size){
			Unit.unitsInRange.get(this.idInUnitsInRange).idInUnitsInRange = this.idInUnitsInRange;
		}
		super.free();
	}
	

	@Override
	public void onDurationExpire() {
		ninja.positionY = 0;
		pool.free(this);
	}
	@Override
	public void onEpilogTrigger(){
		inEpilog = true;
		ninja.speed = Ninja.STANDARD_SPEED;
		ninja.collideAble = true;
	}

	public static void setModel(Model setModel) {
		model = setModel;		
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

}
