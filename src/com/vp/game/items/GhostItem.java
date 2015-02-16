package com.vp.game.items;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Pool;
import com.vp.game.units.Ninja;
import com.vp.game.units.Unit;

public class GhostItem extends Item {
	
	public final static float STANDARD_RADIUS = 6; 
	public final static float STANDARD_DURATION = 5; 
	
	public static boolean active;
	
	private static Model model;
	public final static Pool<GhostItem> pool = new Pool<GhostItem>(){
		@Override
		protected GhostItem newObject(){
			System.out.println("new GhostItem, still free:" + pool.getFree());
			return new GhostItem();
		}
	};

	public GhostItem() {
		this(STANDARD_RADIUS);
	}

	public GhostItem(float radius) {
		super(radius);	
		this.duration = STANDARD_DURATION;
		itemID=0;
	}

	@Override
	public void update(float delta) {

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
			ninja.obsCollideAble = false;
			durationLeft = this.duration;
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
		pool.free(this);
		ninja.obsCollideAble = true;
	}
	
	public static void setModel(Model setModel) {
		model = setModel;		
	}

	@Override
	public Model getModel() {
		return model;
	}
}
