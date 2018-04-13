package com.colin29.hotkeytrainer.data;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Contains a set of hotkeys to be practiced
 * @author Colin Ta
 *
 */
public class Deck implements java.io.Serializable {
	
	private static final long serialVersionUID = 2;
	
	public ArrayList<Card> hotkeys = new ArrayList<Card>();
	
	public transient boolean hasUnsavedChanges;
	
	public void add(Card k){
		hotkeys.add(k);
	}
	
	public ListIterator<Card> listIterator(){
		return hotkeys.listIterator();
	}
}
