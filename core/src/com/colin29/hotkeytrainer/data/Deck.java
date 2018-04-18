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
	private String name = "";
	
	public transient boolean hasUnsavedChanges;
	public transient String diskPath; // path from which that deck was loaded from, or null.
	
	
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
		hotkeys = new Array<Card>();
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
