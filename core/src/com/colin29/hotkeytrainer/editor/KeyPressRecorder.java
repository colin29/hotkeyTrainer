package com.colin29.hotkeytrainer.editor;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.colin29.hotkeytrainer.data.Card;
import com.colin29.hotkeytrainer.data.KeyPress;
import com.colin29.hotkeytrainer.data.KeyPress.ModifierKey;
import com.colin29.hotkeytrainer.util.MyUI.VoidInterface;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Array;

public class KeyPressRecorder extends VisWindow implements InputProcessor {

	InputMultiplexer multiplexer;
	private Label curInputText;
	private Card card; // card that is being constructed
	private Array<KeyPress> keyPresses = new Array<KeyPress>();

	private static String CLEAR_STRING = "{Blank}";

	RecordListener completedListener;

	public KeyPressRecorder(InputMultiplexer multiplexer, Skin skin) {
		super("Input a key combination:");

		this.multiplexer = multiplexer;
		multiplexer.addProcessor(0, this);

		// Create Self
		curInputText = new Label(CLEAR_STRING, skin);
		row();
		add(curInputText);
		row();
		add(new Label("Press Enter to Add Card", skin));
		row();
		add(new Label("Esc to Quit", skin));

	}

	public void setCompletedListener(RecordListener completedListener) {
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
			if (completedListener != null) {
				completedListener.submit(keyPresses); // empty submission is allowed and can represent "recording
														// cancelled"
			}
			keyPresses = new Array<KeyPress>();
			curInputText.setText(CLEAR_STRING);
			curInputText.pack();
			this.pack();
			return true;
		}
		if (keyCode == Keys.ESCAPE) {
			dispose();
			completedListener.complete();
			return true;
		}
		if (keyCode == Keys.BACKSPACE) {
			if (keyPresses.size > 0) {
				keyPresses.removeIndex(keyPresses.size - 1);
			}
			rebuildInputText();
			return true;
		}

		// Make a keypress object
		KeyPress kp = new KeyPress(UIUtils.ctrl(), UIUtils.shift(), UIUtils.alt(), keyCode);

		// Edit the card's contents

		keyPresses.add(kp);

		// if (keyPresses.size == 0) {
		// keyPresses.add(kp);
		// } else {
		// keyPresses.set(0, kp); // Note: Multiple keyPresses in one card not supported yet.
		// }

		// Refresh the text display:
		rebuildInputText();

		return true;
	}

	public void rebuildInputText() {
		String str = "{";
		for (int i = 0; i < keyPresses.size; i++) {
			if (i < keyPresses.size - 1) {
				str += keyPresses.get(i).toString() + ",";
			} else {
				str += keyPresses.get(i).toString();
			}
		}
		str += "}";
		curInputText.setText(str);
		curInputText.pack();
		this.pack();
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

		// Make a keypress object

		KeyPress kp = null;
		if (button == Buttons.LEFT) {
			kp = new KeyPress(UIUtils.ctrl(), UIUtils.shift(), UIUtils.alt(), KeyPress.MOUSE_LEFT);
		} else if (button == Buttons.RIGHT) {
			kp = new KeyPress(UIUtils.ctrl(), UIUtils.shift(), UIUtils.alt(), KeyPress.MOUSE_RIGHT);
		}
		if (kp == null) {
			return false;
		}

		// Edit the card's contents
		keyPresses.add(kp);

		rebuildInputText();

		return true;
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

	public interface RecordListener {
		public void submit(Array<KeyPress> keyPresses);

		public void complete();
	}

}
