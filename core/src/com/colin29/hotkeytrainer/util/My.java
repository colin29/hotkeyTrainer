package com.colin29.hotkeytrainer.util;

import com.badlogic.gdx.graphics.Color;

/**
 * Config file with settings
 * @author Colin Ta
 *
 */
public class My {
	public static final String mapsDirectory = MyIO.getCanonicalPath(".");
	public static final String appTitle = "HotkeyTrainer";
	
	// UI
	public static final float optionButtonSpacing = 10;
	
	public static Color MAIN_BG_COLOR = new Color(1f, 1f, 1f, 1); //new Color(0.97f, 0.97f, 0.95f, 1);
	public static Color SECONDARY_COLOR = new Color(1f, 1f, 1f, 1); //new Color(0.95f, 0.97f, 0.95f, 1);
}
