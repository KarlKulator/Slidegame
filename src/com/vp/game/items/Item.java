package com.vp.game.items;

import com.vp.game.units.HashedUnit;
import com.vp.game.units.Ninja;

public abstract class Item extends HashedUnit {
	
	public float durationLeft;
	protected float duration;
	public int itemID;
	public boolean inEpilog;
	public boolean hasEpilog;

	//The ninja currently holding the item or null if not held
	protected Ninja ninja;

	public Item() {
		super();
	}

	public Item(float radius) {
		super(radius);
	}
	
	public Item(float radius, String animation) {
		super(radius, animation);
	}
	
	public void free(){
		super.free();
	}

	public abstract void onCollect(Ninja ninja);
	

	public abstract void onDurationExpire();
	
	public void updateDuringActive(float delta){}
	
	public boolean updateDuringEpilog(float delta){return true;}
	public void onEpilogTrigger(){}
}
