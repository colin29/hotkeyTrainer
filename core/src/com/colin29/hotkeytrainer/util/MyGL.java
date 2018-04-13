package com.colin29.hotkeytrainer.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class MyGL {
	public static void clearScreen(float r, float b, float g){
		Gdx.gl.glClearColor(r, b, g, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
	}
	public static void setBgColor(Label label, Color color){
		Pixmap labelColor = new Pixmap((int) label.getWidth(), (int) label.getHeight(), Pixmap.Format.RGB888);
		labelColor.setColor(color);
		labelColor.fill();
		label.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
		
		//TODO: Should dispose of pixmap, when? 
	}
}
