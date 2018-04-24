package com.colin29.hotkeytrainer.data;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.colin29.hotkeytrainer.data.KeyPress.ModifierKey;


/**
 * Represents a single key combination. Eg. (ctrl+shift+a). Cannot be modified after creation.
 * @author Colin Ta
 *
 */
public class KeyPress implements java.io.Serializable {

	public enum ModifierKey {
		NONE, CTRL, SHIFT, ALT;
	
		public String toString() {
			if (this == CTRL) {
				return "CTRL";
			} else {
				return "";
			}
		}
	}
	
	public static final int MOUSE_LEFT = Keys.SOFT_LEFT;
	public static final int MOUSE_RIGHT = Keys.SOFT_RIGHT;
	
	private static final long serialVersionUID = 1;
	
	private boolean ctrl;
	private boolean shift;
	private boolean alt;
	
	private int keyCode;
	
	
	public KeyPress(int keyCode){
		this(KeyPress.ModifierKey.NONE, keyCode);
	}
	
	public KeyPress(KeyPress.ModifierKey modifier, int keyCode){
		ctrl = (modifier == KeyPress.ModifierKey.CTRL); 
		shift = (modifier == KeyPress.ModifierKey.SHIFT);
		alt = (modifier == KeyPress.ModifierKey.ALT);
		
		this.keyCode = keyCode;
	}
	public KeyPress(boolean ctrl, boolean shift, boolean alt, int keyCode){
		this.ctrl = ctrl;
		this.shift = shift;
		this.alt = alt;
		this.keyCode = keyCode;
	}
	
	@SuppressWarnings("unused")
	private KeyPress(){ //dummy constructor for use by Kryo
		this(false, false, false, 0);
	}

	public static boolean isAModifierKey(int keyCode){
		int[] a = new int[] { Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT, Keys.ALT_LEFT, Keys.ALT_RIGHT, Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT};
		return false;
	}
	
	public String toString(){
		String s = Keys.toString(keyCode);
		
		switch (keyCode){
		case MOUSE_LEFT:
			s = "Click";
			break;
		case MOUSE_RIGHT:
			s= "RClick";
		}
		
		//Prepend modifiers in reverse order
		s = alt ? "ALT + " + s : s;
		s = shift ? "SHIFT + " + s : s;
		s = ctrl ? "CTRL + " + s : s;
		
		return s;
	}
	
	public boolean ctrl(){
		return ctrl;
	}
	public boolean shift(){
		return shift;
	}
	public boolean alt(){
		return alt;
	}
	public int keyCode(){
		return keyCode;
	}
	@Override
	public boolean equals(Object object){
		if (object instanceof KeyPress){
			KeyPress other = (KeyPress) object;
			if(ctrl == other.ctrl && 
					shift == other.shift &&
					alt == other.alt && keyCode == other.keyCode
					){
				return true;
			}
		}
		return false;
	}
}
