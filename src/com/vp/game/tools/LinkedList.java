package com.vp.game.tools;

public class LinkedList<E extends ListElement> {
	
	ListElement head;
	ListElement tail;
	
	public void add(E e){
		if(head == null){
			head = e;
			tail = e;
			e.setNext(null);
			e.setPrev(null);
		}else{
			tail.setNext(e);
			e.setPrev(tail);
			tail = e;
		}
	}

	public void remove(E e){
		ListElement next = e.getNext();
		ListElement prev = e.getPrev();
		if(prev==null){
			head = next;
			if(head == null){
				tail = null;
			}else{
				head.setPrev(null);
			}
		}else{
			if(next == null){
				prev.setNext(null);
				tail = prev;
			}else{				
				prev.setNext((next));
				next.setPrev(prev);			
			}
		}
	}
}
