package com.colin29.hotkeytrainer.data;



import com.badlogic.gdx.Input.Keys;
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
		String str = "";
		for(int i=0; i<items.size;i++){
			if(i<items.size-1){
				str += items.get(i).toString() + ", ";
			}else{
				str += items.get(i).toString();
			}
		}
		return str;
	}
	
	/**
	 * returns true if two cards have the save contents
	 */
	public boolean sameContents(Object other){
		if(other instanceof Card){
			if(items.equals(((Card) other).getItems())){
				return true;
			}
		}
		return false;
	}
	
	static {
		Card c1 = new Card(new KeyPress(KeyPress.ModifierKey.CTRL, Keys.NUM_5));
		Card c2 = new Card(new KeyPress(Keys.NUM_9));
		
		Array<KeyPress> k3 = new Array<KeyPress>();
		k3.add(new KeyPress(Keys.NUM_3), new KeyPress(Keys.NUM_4));
		Array<KeyPress> k4 = new Array<KeyPress>();
		k4.add(new KeyPress(Keys.NUM_3), new KeyPress(Keys.NUM_4));
		Array<KeyPress> k5 = new Array<KeyPress>();
		k5.add(new KeyPress(Keys.NUM_3), new KeyPress(false, false, true, Keys.NUM_4));
		
		Card c3 = new Card(k3);
		Card c4 = new Card(k4);
		Card c5 = new Card(k5);
		System.out.println();
		
		assert c1.sameContents(c1);
		assert c2.sameContents(c2);
		assert !c1.sameContents(c2);
		
		assert c3.sameContents(c3);
		assert c3.sameContents(c4);
		assert !c3.sameContents(c5);
	}
}
