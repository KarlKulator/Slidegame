package com.vp.game.tools;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Array;

public class WrappingArray<E>{
	
	private final Array<E> underlyingList;
	//The index in array of the element on the left
	private int leftIndex;
	//The index in array of the element on the right
	private int rightIndex;
	
	//The number of elements to be managed
	private final int size;

	public WrappingArray(int size) {
		this.size = size;		
		this.underlyingList = new Array<E>(false, size);
		for(int i = 0; i< size; i++){
			underlyingList.add(null);
		}
		this.rightIndex = size-1;
	}
	
	public void wrapRight(){
		leftIndex=(leftIndex==(size-1))?0:leftIndex+1;
		rightIndex=(rightIndex==(size-1))?0:rightIndex+1;
	}
	
	public void wrapLeft(){
		rightIndex=(rightIndex==0)?(size-1):rightIndex-1;
		leftIndex=(leftIndex==0)?(size-1):leftIndex-1;	
	}
	
	public E get(int index){
		return underlyingList.get((leftIndex+index)%size);
	}
	
	public E getFirst(){
		return underlyingList.get(leftIndex);
	}
	
	public E getLast(){
		return underlyingList.get(rightIndex);
	}
	
	public void set(int index, E element){
		underlyingList.set((leftIndex+index)%size, element);
	}
	
	public int getSize() {
		return size;
	}
}
