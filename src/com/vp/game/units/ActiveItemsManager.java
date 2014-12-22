package com.vp.game.units;

import com.badlogic.gdx.utils.Array;
import com.vp.game.worldelements.WorldElement;

public class ActiveItemsManager implements WorldElement {

	private final Array<Item> activeItems;
	
	public ActiveItemsManager() {
		activeItems = new Array<Item>(false, 5);
	}

	@Override
	public void update(float delta) {
		for(int i = 0; i < activeItems.size; i++){
			Item item = activeItems.get(i);
			item.durationLeft-=delta;
			if(item.durationLeft <= 0){
				item.onDurationExpire();
				activeItems.removeIndex(i);
				i--;
			}
		}
	}
	
	public void addItem(Item item){
		activeItems.add(item);
	}
	
	public void refreshDuration(int itemID){
		for(int i = 0 ; i < activeItems.size; i++){
			Item item = activeItems.get(i);
			if(item.itemID == itemID){
				item.durationLeft = item.duration;
			}
		}
	}
	
	public Item getItemByID(int itemID){
		for(int i = 0 ; i < activeItems.size; i++){
			Item item = activeItems.get(i);
			if(item.itemID == itemID){
				return item;
			}
		}
		return null;
	}
}
