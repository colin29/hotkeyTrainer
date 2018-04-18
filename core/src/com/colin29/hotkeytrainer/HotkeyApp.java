package com.colin29.hotkeytrainer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.colin29.hotkeytrainer.ReviewScreen.ReviewSettings;
import com.colin29.hotkeytrainer.data.Card;
import com.colin29.hotkeytrainer.data.Deck;
import com.colin29.hotkeytrainer.data.KeyPress;
import com.colin29.hotkeytrainer.editor.DeckEditorScreen;
import com.colin29.hotkeytrainer.util.KeyTracker;
import com.colin29.hotkeytrainer.util.MyIO;
import com.esotericsoftware.kryo.Kryo;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;

public class HotkeyApp extends Game implements InputProcessor {
	public static float VOLUME_SFX = 1f;

	SpriteBatch batch;

	// Rendering and pipeline variables

	// Scene2D
	private Stage stage;

	public Skin skin;
	BitmapFont font_size1;
	BitmapFont font_size2;
	
	BitmapFont font2_size1;
	BitmapFont font2_size2;

	// Input
	InputMultiplexer multiplexer = new InputMultiplexer();

	public FileChooser fileChooser;

	// Program Logic
	public ReviewSettings settings = new ReviewSettings();

	// Screens:
	public DecksMenuScreen decksMenu;
	public DeckEditorScreen deckEditor;
	public ReviewScreen reviewScreen;
	
	// Serialization
	public Kryo kryo = new Kryo();
	{
		MyIO.registerSerializers(kryo);
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		printDirectory(".");

		stage = new Stage();

		// Load skins and fonts
		VisUI.load();
		skin = VisUI.getSkin();

		initFileChooser();

		initFonts();

		initScreens();
		
		initSounds();

//		this.setScreen(deckEditor);
		this.setScreen(decksMenu);
		
//		ReviewScreenSettings settings = new ReviewScreenSettings();
//		settings.randomOrder = true;
//		
//		this.setScreen(new ReviewScreen(makeTestDeck(), settings, this));
		


	}


	private void initScreens() {
		decksMenu = new DecksMenuScreen(this);
		deckEditor = new DeckEditorScreen(this);
	}

	private void initFileChooser() {
		FileChooser.setDefaultPrefsName("holowyth.test.ui.filechooser");
		fileChooser = new FileChooser(Mode.OPEN);
		fileChooser.setSize(480, 480);
	}

	private void initFonts() {
		font_size1 = generateFont("fonts/OpenSans.ttf", Color.BLACK, 32);
		font_size2 = generateFont("fonts/OpenSans.ttf", Color.BLACK, 16);
		
		
		font2_size1 = generateFont("fonts/OpenSans.ttf", Color.WHITE, 32);
		font2_size2 = generateFont("fonts/OpenSans.ttf", Color.WHITE, 16);		
	}
	
	Sound chime;
	private void initSounds(){
		chime = Gdx.audio.newSound(Gdx.files.internal("sfx/affirmative-decision-chime.wav"));
	}
	
	private Deck makeTestDeck(){
		Array<Card> array = new Array<Card>();
		array.add(new Card(new KeyPress(KeyPress.ModifierKey.CTRL, Keys.NUM_5)));
		array.add(new Card(new KeyPress(Keys.NUM_9)));
		array.add(new Card(new KeyPress(Keys.NUM_3)));
		
		Deck testDeck = new Deck(array);
		return testDeck;
	}

	@Override
	public void render() {
		super.render(); // Calls Game.render, which will render the screens
		VOLUME_SFX = settings.soundOn ? 1f : 0;
	}

	@Override
	public void dispose() {
		batch.dispose();
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
		lastKeyPressText.setPosition(Gdx.graphics.getWidth() - lastKeyPressText.getWidth() - 20, 20);
		stage.addActor(lastKeyPressText);

		hotkeyText.debug();
		lastKeyPressText.debug();

	}

	


	

	final int[] TRACKED_KEYS = new int[] { Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT, Keys.ALT_LEFT, Keys.ALT_RIGHT,
			Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT };
	KeyTracker keyTracker = new KeyTracker(TRACKED_KEYS, multiplexer);


	

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
		System.out.println("Directory: " + System.getProperty("user.dir"));
		// System.out.println("Directory\n---");
		System.out.println("---");
		FileHandle h = new FileHandle(dir);
		FileHandle[] files = h.list();
		for (FileHandle f : files) {
			System.out.println(f.name());
		}
		System.out.println("" + "---");
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

}
