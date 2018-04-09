package com.colin29.hotkeytrainer;

import com.badlogic.gdx.Input.Keys;

public class KeyPress implements java.io.Serializable {

	private static final long serialVersionUID = 1;
	
	public final boolean ctrl;
	public final boolean shift;
	public final boolean alt;
	
	public final int keyCode;
	
	public KeyPress(int keyCode){
		this(HotkeyTrainer.KeyModifier.NONE, keyCode);
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
	
}
