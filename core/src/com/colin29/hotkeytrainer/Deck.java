package com.colin29.hotkeytrainer;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Contains a set of hotkeys to be practiced
 * @author Colin Ta
 *
 */
public class Deck implements java.io.Serializable {
	
	private static final long serialVersionUID = 1;
	
	public ArrayList<KeyPress> hotkeys = new ArrayList<KeyPress>();
	
	public transient boolean hasUnsavedChanges;
	
	public void add(KeyPress k){
		hotkeys.add(k);
	}
	
	public ListIterator<KeyPress> listIterator(){
		return hotkeys.listIterator();
	}
}
