package com.vp.game.units;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Pool;

public class FlyItem extends Item {
	
	public final static float STANDARD_RADIUS = 6; 
	public final static float STANDARD_DURATION = 5; 
	
	public static boolean active;
	
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
		this.duration = STANDARD_DURATION;
		itemID=1;
	}

	public FlyItem(float radius, String animation) {
		super(radius, animation);
		this.duration = STANDARD_DURATION;
		itemID=1;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void free() {
		super.free();	
		pool.free(this);
	}
	
	@Override
	public void onCollect() {
		if(!FlyItem.active){
			FlyItem.active = true;
			Ninja.mainNinja.activeItems.addItem(this);
			Ninja.mainNinja.collideAble = false;
			Ninja.mainNinja.positionY = 30;
			durationLeft = this.duration;
		}else{
			Ninja.mainNinja.activeItems.refreshDuration(itemID);
		}
		Unit.unitsInRange.removeIndex(this.idInUnitsInRange);
		if(this.idInUnitsInRange < Unit.unitsInRange.size){
			Unit.unitsInRange.get(this.idInUnitsInRange).idInUnitsInRange = this.idInUnitsInRange;
		}
		free();
	}

	@Override
	public void onDurationExpire() {
		active = false;
		Ninja.mainNinja.positionY = 0;
		Ninja.mainNinja.collideAble = true;
	}

	public static void setModel(Model setModel) {
		model = setModel;		
	}

	@Override
	public Model getModel() {
		return model;
	}

}
