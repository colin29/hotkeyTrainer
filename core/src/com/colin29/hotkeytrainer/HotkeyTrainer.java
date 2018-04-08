package com.colin29.hotkeytrainer;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.colin29.hotkeytrainer.util.KeyTracker;
import com.kotcrab.vis.ui.VisUI;

public class HotkeyTrainer extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;

	// Rendering and pipeline variables

	// Scene2D
	private Stage stage;

	Skin skin;
	BitmapFont font_size1;
	BitmapFont font_size2;

	// Input
	InputMultiplexer multiplexer = new InputMultiplexer();

	@Override
	public void create() {
		batch = new SpriteBatch();
		printDirectory(".");
		img = new Texture("badlogic.jpg");

		stage = new Stage();
		stage.setDebugAll(true);

		// Load skins and fonts
		VisUI.load();
		skin = VisUI.getSkin();

		initFonts();

		// Create UI
		createUI();
		initHotkeyTrainer();

		// Set Input
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);

	}

	private void initFonts() {
		font_size1 = generateFont("fonts/OpenSans.ttf", Color.BLACK, 32);
		font_size2 = generateFont("fonts/OpenSans.ttf", Color.BLACK, 16);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		stage.draw();
		// Render some text

		// Run Program Logic:
		runHotkeyTrainer();

	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
		VisUI.dispose();
	}

	// UI
	Label hotkeyText;
	Label lastKeyPressText;

	private void createUI() {

		Label.LabelStyle labelStyle = new Label.LabelStyle(font_size1, Color.BLACK);
		Label.LabelStyle labelStyle2 = new Label.LabelStyle(font_size2, Color.BLACK);

		hotkeyText = new Label("Enter this Hotkey", labelStyle);
		hotkeyText.setColor(Color.BLACK);
		hotkeyText.setPosition(Gdx.graphics.getWidth() / 2 - hotkeyText.getWidth() / 2,
				Gdx.graphics.getHeight() * 0.66f);
		hotkeyText.setAlignment(Align.center);
		stage.addActor(hotkeyText);

		lastKeyPressText = new Label("No Key Pressed Yet", labelStyle2);
		lastKeyPressText.setColor(Color.BLACK);
		stage.addActor(lastKeyPressText);
		lastKeyPressText.setPosition(Gdx.graphics.getWidth() - lastKeyPressText.getWidth() - 20, 20);

	}

	// Application

	public enum KeyModifier {
		NONE, CTRL, SHIFT, ALT;

		public String toString() {
			if (this == CTRL) {
				return "CTRL";
			} else {
				return "";
			}
		}
	}

	private boolean hotkeyCompleted = false;
	String keyCombo;
	KeyModifier modifier;
	KeyPress curHotkey;
	
	ArrayList<KeyPress> hotkeyList = new ArrayList<KeyPress>();
	private boolean done = false;
	ListIterator<KeyPress> hotkeyIter;
	
	private void initHotkeyTrainer() {
		keyCombo = "5";
		modifier = KeyModifier.CTRL;
		
		hotkeyList.add(new KeyPress(KeyModifier.CTRL, Keys.NUM_4));
		hotkeyList.add(new KeyPress(Keys.NUM_9));
		hotkeyList.add(new KeyPress(Keys.NUM_6));
		hotkeyIter = hotkeyList.listIterator();

	}

	private void runHotkeyTrainer() {

		if(done){
			return;
		}
		
		if(hotkeyList.isEmpty()){
			System.out.println("Hotkey list given was empty");
			done = true;
			return;
		}
		if(hotkeyIter.nextIndex() == 0){
			curHotkey = hotkeyIter.next();
			hotkeyText.setText(curHotkey.toString());
			hotkeyCompleted = false;
		}

		if (hotkeyCompleted) {
			if(hotkeyIter.hasNext()){
				curHotkey = hotkeyIter.next();
				hotkeyText.setText(curHotkey.toString());
				hotkeyCompleted = false;
			}else{
				curHotkey = null;
				System.out.println("Done!");
				hotkeyText.setText("Finished");
				done = true;
			}
		}
	}


	final int[] TRACKED_KEYS = new int[] { Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT, Keys.ALT_LEFT, Keys.ALT_RIGHT,
			Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT };
	KeyTracker keyTracker = new KeyTracker(TRACKED_KEYS, multiplexer);
	static final Integer[] modifierKeys = { Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT, Keys.ALT_LEFT, Keys.ALT_RIGHT,
			Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT };

	@Override
	public boolean keyDown(int keyCode) {

		if(isModifierKey(keyCode)){
			return false;
		}

		//Make a keypress object
		KeyPress pressed = new KeyPress(isModifierPressed(KeyModifier.CTRL),
				isModifierPressed(KeyModifier.SHIFT),
				isModifierPressed(KeyModifier.ALT),
				keyCode);
				
		System.out.printf("Key pressed: %s\n", pressed.toString());
		
		
		lastKeyPressText.setText(pressed.toString());
		
		if (!hotkeyCompleted && curHotkey != null) {
			if (curHotkey.keyCode == keyCode && 
					(curHotkey.ctrl == isModifierPressed(KeyModifier.CTRL)
					&& curHotkey.shift == isModifierPressed(KeyModifier.SHIFT)
					&& curHotkey.alt == isModifierPressed(KeyModifier.ALT))) {
				hotkeyCompleted = true;
				return true;
			}
		}
		return false;
	}

	private boolean isModifierPressed(KeyModifier modifier) {
		switch (modifier) {
		case CTRL:
			return (modifier == KeyModifier.CTRL
					&& (keyTracker.isKeyDown(Keys.CONTROL_LEFT) || keyTracker.isKeyDown(Keys.CONTROL_RIGHT)));
		case SHIFT:
			return (modifier == KeyModifier.SHIFT
					&& (keyTracker.isKeyDown(Keys.SHIFT_LEFT) || keyTracker.isKeyDown(Keys.SHIFT_RIGHT)));
		case ALT:
			return (modifier == KeyModifier.CTRL
					&& (keyTracker.isKeyDown(Keys.ALT_LEFT) || keyTracker.isKeyDown(Keys.ALT_RIGHT)));
		case NONE:
			return true;
		default:
			throw new RuntimeException("Unhandled Case");
		}
	}

	private boolean isModifierKey(int keyCode){
		if (Arrays.asList(modifierKeys).contains(keyCode)) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	// Utility functions (not specific to this project)

	private BitmapFont generateFont(String path, Color color, int size) {
		FreeTypeFontGenerator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.color = Color.WHITE;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}

	public static void printDirectory(String dir) {
		System.out.println(System.getProperty("user.dir"));

		System.out.println("Directory\n---");
		FileHandle h = new FileHandle(dir);
		FileHandle[] files = h.list();
		for (FileHandle f : files) {
			System.out.println(f.name());
		}
		System.out.println("" + "---");
	}

}
