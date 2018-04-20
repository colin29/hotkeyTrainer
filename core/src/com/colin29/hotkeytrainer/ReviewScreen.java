package com.colin29.hotkeytrainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.colin29.hotkeytrainer.HotkeyApp;
import com.colin29.hotkeytrainer.data.Card;
import com.colin29.hotkeytrainer.data.Deck;
import com.colin29.hotkeytrainer.data.KeyPress;
import com.colin29.hotkeytrainer.util.My;
import com.colin29.hotkeytrainer.util.MyGL;
import com.colin29.hotkeytrainer.util.MyUI.HeaderTable;

/**
 * Screen where users can see all their decks at a glance
 * 
 * @author Colin Ta
 *
 */
public class ReviewScreen implements Screen, InputProcessor {

	HotkeyApp app;

	// UI
	private Stage stage = new Stage();
	private Table root;
	private Skin skin;

	// Input
	InputMultiplexer multiplexer = new InputMultiplexer();

	// Logic
	Deck deck;
	ReviewSettings settings;

	ReviewScreen(Deck deck, ReviewSettings settings, HotkeyApp app) {
		this.app = app;
		this.skin = app.skin;

		this.deck = deck;
		this.settings = settings;

		
		initSkins();
		// Init Logic
		initHotkeyTrainer();

		createUI();
	}

	public void initSkins(){
		labelStyle = new Label.LabelStyle(app.font_size1, null);
		app.font_size1.getData().markupEnabled = true;
		
		labelStyle2 = new Label.LabelStyle(app.font_size2, Color.BLACK);
	}
	
	public void createUI() {
		
		
		// Create root with three sections on top of each other (3x1)
		root = new Table();
		root.setFillParent(true);

		HeaderTable header;
		Table body = new Table();
		Table footer = new Table();

		// Create header
		header = new HeaderTable(skin);

		header.decksButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				app.setScreen(app.decksMenu);
			}
		});
		header.editButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				app.setScreen(app.deckEditor);
			}
		});

		createBody(body);

		// Assemble root table
		root.row().expandX(); // Have all sections expand to width of the screen
		root.add(header).fillX();
		root.row().expandY().align(Align.topLeft); // Body section expands to cover main part of screen
		root.add(body);
		root.row().align(Align.left);
		root.add(footer);
		root.pack();

		stage.addActor(root);
		
		createHotkeyLabels();

	}

	public void createBody(Table body) {

	}
	
	/**
	 * Adds hotkey labels directly to the stage
	 */
	public void createHotkeyLabels(){

		hotkeyText = new Label("", labelStyle);
		hotkeyText.setPosition(Gdx.graphics.getWidth() / 2 - hotkeyText.getWidth() / 2,
				Gdx.graphics.getHeight() * 0.66f);
		hotkeyText.setAlignment(Align.center);
		stage.addActor(hotkeyText);

		lastKeyPressText = new Label("No Key Pressed Yet", labelStyle2);
		lastKeyPressText.setPosition(Gdx.graphics.getWidth() - lastKeyPressText.getWidth() - 20, 10);
//		lastKeyPressText.setPosition(Gdx.graphics.getWidth() / 2 - lastKeyPressText.getWidth() / 2,
//				hotkeyText.getY()-200);
		lastKeyPressText.setAlignment(Align.center);
		stage.addActor(lastKeyPressText);

		
		doneMessage = new Label("Press Enter to return, or R to start again", labelStyle2);
		doneMessage.setPosition(Gdx.graphics.getWidth() / 2 - doneMessage.getWidth() / 2,
				hotkeyText.getY()-50);
		doneMessage.setVisible(false);
		System.out.printf("Done Message pos: %s %s \n", doneMessage.getX(), doneMessage.getY());
		stage.addActor(doneMessage);
		
		
//		hotkeyText.debug();
//		lastKeyPressText.debug();
	}

	// Review logic

	ArrayList<KeyPress> hotkeyList = new ArrayList<KeyPress>();
	private boolean done = false;
	Iterator<Card> hotkeyIter;

	private boolean started = false;
	
	/**
	 * Displays what keypress the user should press next
	 */
	private Label hotkeyText;
	// Application

	private boolean cardCompleted = false;
	String keyCombo;
	KeyPress.ModifierKey modifier;
	Array<KeyPress> curCard;

	private Label lastKeyPressText;

	private void initHotkeyTrainer() {
		Array<Card> cardList = new Array<Card>(deck.getHotkeys());
		
		if(app.settings.randomOrder){
			cardList.shuffle();
		}
		
		hotkeyIter = cardList.iterator();
		hotkeyText = new Label("Deck not started.", labelStyle);
	}

	private void runHotkeyTrainer() {
		if (done) {
			return;
		}

		if (!started) {
			if (hotkeyIter.hasNext()) {
				curCard = hotkeyIter.next().getItems();
				rebuildHotkeyText();
				cardCompleted = false;
				started = true;
			} else {
				System.out.println("Hotkey Deck given was empty");
				finishDeck();
				return;
			}
		}

		if (cardCompleted) {
			if (hotkeyIter.hasNext()) {
				curCard = hotkeyIter.next().getItems();
				rebuildHotkeyText();
				cardCompleted = false;
			} else {
				curCard = null;
				System.out.println("Done!");
				finishDeck();
			}
		}
	}

	private void finishDeck() {
		hotkeyText.setText("Finished");
		doneMessage.setVisible(true);
		done = true;
	}

	private int keyPressIndex = 0;
	@Override
	public boolean keyDown(int keyCode) {

		if (isModifierKey(keyCode)) {
			return false;
		}
		
		if(done){
			if (keyCode == Keys.ENTER){
				app.setScreen(new DeckScreen(this.deck, app));
				return true;
			}
			if (keyCode == Keys.R || keyCode == Keys.GRAVE){
				app.setScreen(new ReviewScreen(this.deck, this.settings, app));
				return true;
			}
		}
		
		if (keyCode == Keys.ESCAPE){
			app.setScreen(new DeckScreen(this.deck, app));
			return true;
		}
		
		// Make a keypress object
		KeyPress pressed = new KeyPress(UIUtils.ctrl(), UIUtils.shift(), UIUtils.alt(), keyCode);

		System.out.printf("Key pressed: %s\n", pressed.toString());

		lastKeyPressText.setText(pressed.toString());

		// See if the key pressed was the one required
		if (!cardCompleted && curCard != null) {
			KeyPress curKeyPress = curCard.get(keyPressIndex);
			if (curKeyPress.keyCode() == keyCode && (curKeyPress.ctrl() == UIUtils.ctrl()
					&& curKeyPress.shift() == UIUtils.shift() && curKeyPress.alt() == UIUtils.alt())) {
				
				keyPressIndex++;
				rebuildHotkeyText();
				app.chime.play(app.VOLUME_SFX);	
				
				if(keyPressIndex == curCard.size){
					cardCompleted = true;
					keyPressIndex = 0;
				}
				return true;
			}
		}
		return false;
	}
	
	private void rebuildHotkeyText(){
		String str = "[BLACK][#50c878]";
		for(int i=0; i<curCard.size;i++){
			if(i==keyPressIndex){
				str += "[]"; //highlight the completed portion green
			}
			if(i<curCard.size-1){
				str += curCard.get(i).toString() + ", ";
			}else{
				str += curCard.get(i).toString();
			}
		}
//		str += "[]"; //highlight the completed portion green
		System.out.println(str);
		hotkeyText.setText(str);
//		hotkeyText.setText("[#ff0000]l[#30ff00]i[#1e00ff]b[#fff600]G[#ff00ae]D[][#ff9000]");
		

	}

	// private boolean isModifierPressed(KeyPress.ModifierKey modifier) {
	// switch (modifier) {
	// case CTRL:
	// return (modifier == KeyPress.ModifierKey.CTRL
	// && UIUtils.ctrl());
	// case SHIFT:
	// return (modifier == KeyPress.ModifierKey.SHIFT
	// && UIUtils.shift());
	// case ALT:
	// return (modifier == KeyPress.ModifierKey.ALT
	// && UIUtils.ctrl());
	// case NONE:
	// return true;
	// default:
	// throw new RuntimeException("Unhandled Case");
	// }
	// }
	static final Integer[] modifierKeys = { Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT, Keys.ALT_LEFT, Keys.ALT_RIGHT,
			Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT };

	private Label doneMessage;

	private Label.LabelStyle labelStyle;

	private Label.LabelStyle labelStyle2;

	private boolean isModifierKey(int keyCode) {
		if (Arrays.asList(modifierKeys).contains(keyCode)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void show() {
		System.out.println("Showed Review Screen");
		multiplexer.clear();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		MyGL.clearScreen(My.MAIN_BG_COLOR.r, My.MAIN_BG_COLOR.g, My.MAIN_BG_COLOR.b);
		stage.act(delta);
		stage.draw();
		
		runHotkeyTrainer();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

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
	
	static class ReviewSettings{
		public boolean randomOrder = true;
		public boolean endlessReview = false; // When enabled, the review session will keep looping until the user manually exits
		public boolean soundOn = true;
		
	}

}
