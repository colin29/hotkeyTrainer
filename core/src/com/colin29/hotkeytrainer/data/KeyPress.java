package com.colin29.hotkeytrainer.data;

import com.badlogic.gdx.Input.Keys;
import com.colin29.hotkeytrainer.HotkeyTrainer;
import com.colin29.hotkeytrainer.HotkeyTrainer.KeyModifier;

public class KeyPress implements java.io.Serializable {

	private static final long serialVersionUID = 1;
	
	private boolean ctrl;
	private boolean shift;
	private boolean alt;
	
	private int keyCode;
	
	
	public KeyPress(int keyCode){
		this(HotkeyTrainer.KeyModifier.NONE, keyCode);
	}
	
	private KeyPress(){ //dummy initilization for use by Kryo
		this(false, false, false, 0);
	}
	
	/**
	 * To make a keyPress with more than one modifier, you must use a string constructor
	 * @param modifier
	 * @param keyCode
	 */
	public KeyPress(HotkeyTrainer.KeyModifier modifier, int keyCode){
		ctrl = (modifier == HotkeyTrainer.KeyModifier.CTRL); 
		shift = (modifier == HotkeyTrainer.KeyModifier.SHIFT);
		alt = (modifier == HotkeyTrainer.KeyModifier.ALT);
		
		this.keyCode = keyCode;
	}
	public KeyPress(boolean ctrl, boolean shift, boolean alt, int keyCode){
		this.ctrl = ctrl;
		this.shift = shift;
		this.alt = alt;
		this.keyCode = keyCode;
	}
	
	public static boolean isAModifierKey(int keyCode){
		int[] a = new int[] { Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT, Keys.ALT_LEFT, Keys.ALT_RIGHT, Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT};
		return false;
	}
	
	public String toString(){
		String s = Keys.toString(keyCode);
		
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
}
