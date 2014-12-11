package com.vp.game.tools;

public interface ListElement {
	public ListElement getNext();
	public ListElement getPrev();
	public void setNext(ListElement next);
	public void setPrev(ListElement prev);
}
