package com.colin29.hotkeytrainer.data;

import java.util.ArrayList;

import com.colin29.hotkeytrainer.util.exception.ErrorCode;
import com.colin29.hotkeytrainer.util.exception.MyException;

public class Card implements java.io.Serializable {
	private static final long serialVersionUID = 1;

	private ArrayList<KeyPress> items = new ArrayList<KeyPress>();
	
	public Card(ArrayList<KeyPress> kps){
		setItems(kps);
		this.items = kps;
	}

	public Card(KeyPress kp){
		this.items.add(kp);
	}
	
	public boolean isMultiple() {
		return (items.size() > 1);
	}

	public void setItems(ArrayList<KeyPress> kps) {
		if(kps.size()==0){
			throw new MyException(ErrorCode.INVALID_PARAMETERS);
		}
		items = kps;
	}

	public ArrayList<KeyPress> getItems() {
		return items;
	}
	
	/**
	 * Multiple hotkeys are not supported yet: everything is treated like a single keypress card.
	 */
	public KeyPress getSingle(){
		return items.get(0);
	}
	
	public String getKeyPressesAsText(){
		return items.get(0).toString();
	}
}