package com.colin29.hotkeytrainer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.colin29.hotkeytrainer.HotkeyApp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		
		config.samples = 5;
		new LwjglApplication(new HotkeyApp(), config);
	}
}
