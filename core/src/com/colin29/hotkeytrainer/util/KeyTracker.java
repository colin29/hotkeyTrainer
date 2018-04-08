package com.colin29.hotkeytrainer.util;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

/**
 * Tracks whether given keys are being pressed down or not.
 * 
 * @usage Construct a KeyTracker with the keys you would like to track.
 */
public class KeyTracker implements InputProcessor {

	private Map<Integer, Boolean> keyIsDown = new HashMap<Integer, Boolean>();

	public KeyTracker(int[] keysToTrack, InputMultiplexer multiplexer) {
		for (int keycode : keysToTrack) {
			keyIsDown.put(keycode, false);
		}
		
		multiplexer.addProcessor(0, this);
	}

	public boolean isKeyDown(int keycode) {
		if (keyIsDown.containsKey(keycode)) {
			return keyIsDown.get(keycode);
		} else {
			throw new RuntimeException("isKeyDown: Key is not tracked");
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		// For tracked keys, mark the key down if is pressed.
		for (Map.Entry<Integer, Boolean> entry : keyIsDown.entrySet()) {
			if (keycode == entry.getKey()) {
				entry.setValue(true);
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// For tracked keys, mark the key up when it is lifted.
		for (Map.Entry<Integer, Boolean> entry : keyIsDown.entrySet()) {
			if (keycode == entry.getKey()) {
				entry.setValue(false);
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
