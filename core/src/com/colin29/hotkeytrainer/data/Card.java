package com.colin29.hotkeytrainer.data;


import com.badlogic.gdx.utils.Array;
import com.colin29.hotkeytrainer.util.exception.ErrorCode;
import com.colin29.hotkeytrainer.util.exception.MyException;

public class Card implements java.io.Serializable {
	private static final long serialVersionUID = 1;

	private Array<KeyPress> items = new Array<KeyPress>();
	
	private Card(){
	}
	public Card(Array<KeyPress> kps){
		setItems(kps);
		this.items = kps;
	}

	public Card(KeyPress kp){
		this.items.add(kp);
	}
	
	public void setItems(Array<KeyPress> kps) {
		if(kps.size==0){
			throw new MyException(ErrorCode.INVALID_PARAMETERS);
		}
		items.clear();
		items.addAll(kps);
	}

	public boolean isMultiple() {
		return (items.size > 1);
	}
	public Array<KeyPress> getItems() {
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
