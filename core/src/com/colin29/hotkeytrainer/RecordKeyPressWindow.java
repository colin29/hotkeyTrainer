package com.colin29.hotkeytrainer;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.colin29.hotkeytrainer.HotkeyTrainer.KeyModifier;
import com.colin29.hotkeytrainer.data.Card;
import com.colin29.hotkeytrainer.data.KeyPress;
import com.colin29.hotkeytrainer.util.MyUI.VoidInterface;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;

public class RecordKeyPressWindow extends VisWindow implements InputProcessor {

	InputMultiplexer multiplexer;
	private Label lastKeyPressText;
	private Card card; // card that is being constructed
	private ArrayList<KeyPress> keyPresses = new ArrayList<KeyPress>();
	
	private static String CLEAR_STRING = "{Blank}";
	
	RecordListener completedListener;

	public RecordKeyPressWindow(InputMultiplexer multiplexer, Skin skin) {
		super("Input a key combination:");

		this.multiplexer = multiplexer;
		multiplexer.addProcessor(0, this);
		
		//Create Self
		lastKeyPressText = new Label(CLEAR_STRING, skin);
		row();
		add(lastKeyPressText);
		row();
		add(new Label("Press Enter to Add Card", skin));
		row();
		add(new Label("Esc to Quit", skin));
		
	}
	public void setCompletedListener(RecordListener completedListener){
		this.completedListener = completedListener;
	}

	public void dispose() {
		multiplexer.removeProcessor(this);
	}

	@Override
	public boolean keyDown(int keyCode) {
		if (isModifierKey(keyCode)) {
			return true;
		}

		// If the keypress is "enter", submit the keypress
		if (keyCode == Keys.ENTER) {
			System.out.println("Submit");
			if(completedListener != null){
				completedListener.submit(keyPresses); //empty submission is allowed and can represent "recording cancelled"
			}
			keyPresses = new ArrayList<KeyPress>();
			lastKeyPressText.setText(CLEAR_STRING);
			return true;
		}
		if (keyCode == Keys.ESCAPE){
			dispose();
			completedListener.complete();
		}

		// Make a keypress object
		KeyPress kp = new KeyPress(UIUtils.ctrl(), UIUtils.shift(), UIUtils.alt(), keyCode);
		// Display what was just pressed
		lastKeyPressText.setText("{" + kp.toString() + "}");

		// Edit the card's contents
		if (keyPresses.isEmpty()) {
			keyPresses.add(kp);
		} else {
			keyPresses.set(0, kp); // Note: Multiple keyPresses in one card not supported yet.
		}
		return true;
	}

	public boolean isModifierKey(int keyCode) {
		return UIUtils.ctrl(keyCode) || UIUtils.shift(keyCode) || UIUtils.alt(keyCode);
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
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
	
	public interface RecordListener{
		public void submit(ArrayList<KeyPress> keyPresses);
		public void complete();
	}

}
