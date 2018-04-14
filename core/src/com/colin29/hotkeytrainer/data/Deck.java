package com.colin29.hotkeytrainer.data;

import java.util.Iterator;
import com.badlogic.gdx.utils.Array;

/**
 * Contains a set of hotkeys to be practiced
 * @author Colin Ta
 *
 */
public class Deck implements java.io.Serializable {
	
	private static final long serialVersionUID = 2;
	
	private Array<Card> hotkeys = new Array<Card>();
	
	public transient boolean hasUnsavedChanges;
	
	
	
	/**
	 * Creates a Deck with the supplied cards
	 * @param cards
	 */
	public Deck(Array<Card> cards){
		this();
		hotkeys.clear();
		hotkeys.addAll(cards);
	}
	public Deck(){
	}
	
	public void add(Card k){
		hotkeys.add(k);
	}
	
	public Iterator<Card> iterator(){
		return hotkeys.iterator();
	}
	public Array<Card> getHotkeys(){
		return hotkeys;
	}
}
