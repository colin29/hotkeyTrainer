package com.colin29.hotkeytrainer.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.utils.Array;

/**
 * Contains a set of hotkeys to be practiced
 * @author Colin Ta
 *
 */
public class Deck implements java.io.Serializable {
	
	private static final long serialVersionUID = 2;
	
	public Array<Card> hotkeys = new Array<Card>();
	
	public transient boolean hasUnsavedChanges;
	
	
	public Deck(Array<Card> cards){
		this();
		this.hotkeys = cards;
		//TODO: may want to make a deep copy here, investigate
	}
	public Deck(){
	}
	
	public void add(Card k){
		hotkeys.add(k);
	}
	
	public Iterator<Card> iterator(){
		return hotkeys.iterator();
	}
}
