package com.vp.game.units;

public abstract class Item extends HashedUnit {
	
	public float durationLeft;
	protected float duration;
	public int itemID;

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

	public abstract void onCollect();

	public abstract void onDurationExpire();
}
