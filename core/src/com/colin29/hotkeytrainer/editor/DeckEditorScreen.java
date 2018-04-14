package com.colin29.hotkeytrainer.editor;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.colin29.hotkeytrainer.HotkeyApp;
import com.colin29.hotkeytrainer.data.Card;
import com.colin29.hotkeytrainer.data.Deck;
import com.colin29.hotkeytrainer.data.KeyPress;
import com.colin29.hotkeytrainer.data.KeyPress.ModifierKey;
import com.colin29.hotkeytrainer.editor.KeyPressRecorder.RecordListener;
import com.colin29.hotkeytrainer.util.My;
import com.colin29.hotkeytrainer.util.MyGL;
import com.colin29.hotkeytrainer.util.MyIO;
import com.colin29.hotkeytrainer.util.MyUI;
import com.colin29.hotkeytrainer.util.exception.ErrorCode;
import com.colin29.hotkeytrainer.util.exception.MyException;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.badlogic.gdx.files.FileHandle;

public class DeckEditorScreen implements Screen, InputProcessor {

	private HotkeyApp app;
	private Deck deck;
	private FileChooser fileChooser;

	// UI
	private Stage stage = new Stage();
	private Table root;
	private Skin skin;

	// Deck View
	private ListView<Card> deckView;
	private ArrayAdapter<Card, ?> adapter;

	// Input
	InputMultiplexer multiplexer = new InputMultiplexer();

	public DeckEditorScreen(HotkeyApp app) {
		this.app = app;
		fileChooser = app.fileChooser;
		this.skin = app.skin;

		createUI();
		createTestDeck();
	}

	private void createTestDeck() {
		Array<Card> array = new Array<Card>();
		array.add(new Card(new KeyPress(KeyPress.ModifierKey.CTRL, Keys.NUM_5)));
		array.add(new Card(new KeyPress(Keys.NUM_9)));
		array.add(new Card(new KeyPress(Keys.NUM_3)));
		
		Deck testDeck = new Deck(array);
		loadDeck(testDeck);
		putDeckInWindow();
	}

	private void createUI() {

		final float optionButtonSpacing = 10;

		// Create root with three sections on top of each other (3x1)
		root = new Table();
		root.setFillParent(true);

		Table header = new Table();
		Table body = new Table();
		Table footer = new Table();

		// Create header

		header.row().space(0, optionButtonSpacing, 0, optionButtonSpacing);
		MyUI.textButton(header, "Decks", skin, () -> {
		});
		MyUI.textButton(header, "Edit", skin, () -> {
		});
		header.pack();

		MyUI.printDimensions("header", header);

		// Create body
		createBody(body);

		// Create footer
		footer.row().align(Align.left).space(0, optionButtonSpacing, 0, optionButtonSpacing);
		MyUI.textButton(footer, "Save Deck", skin, () -> {
			openFileChooserThenSaveDeck();
		});
		MyUI.textButton(footer, "Load Deck", skin, () -> {
			openFileChooserThenLoadDeck();
		});
		MyUI.textButton(footer, "New Deck", skin, () -> {
		});
		MyUI.textButton(footer, "Deck Properties", skin, () -> {
		});
		footer.pack();

		// Assemble root table
		root.row().expandX(); // Have all sections expand to width of the screen
		root.add(header).fillX();
		root.row().expandY().align(Align.topLeft); // Body section expands to cover main part of screen
		root.add(body);
		root.row().align(Align.left);
		root.add(footer);
		root.pack();

		header.setBackground(MyGL.getBGDrawable(header, MyUI.visSkinColor()));

		// Final tasks
		stage.addActor(root);

		// root.setDebug(true, true);
		// body.setDebug(false, true);

	}

	private void createBody(Table body) {

		DeckDisplay deckWindow = new DeckDisplay();
		this.deckView = deckWindow.getDeckView();
		deckWindow.setMovable(false);

		body.pad(15);
		body.row().spaceRight(30).align(Align.topLeft); // put space between cells, align cell contents

		body.add(deckWindow).width(250).height(300);

		// Create add and delete buttons
		Table deckButtons = new Table();
		deckButtons.defaults().align(Align.topLeft).spaceBottom(15);

		adapter = deckWindow.getRemoveAdapter();

		deckButtons.row();
		MyUI.textButton(deckButtons, "Add Card", skin, this::openRecordHotkeyWindow);
		deckButtons.row();
		MyUI.textButton(deckButtons, "Delete Selected", skin, () -> {
			System.out.println("delete cards");
			Array<Card> selected = new Array<Card>(adapter.getSelection()); // make a copy of the list because the
																			// original may change as we remove
																			// elements.
			for (Card c : selected) {
				adapter.removeValue(c, true);
			}

		});

		body.add(deckButtons);

	}

	/**
	 * Opens a hotkey recording window, then add any cards recorded
	 */
	private void openRecordHotkeyWindow() {
		KeyPressRecorder record = new KeyPressRecorder(multiplexer, skin);
		record.setCompletedListener(new RecordListener() {
			@Override
			public void submit(Array<KeyPress> keyPresses) {
				// Make a card and add it to the collection.
				if (!(keyPresses.size == 0)) {
					Card newCard = new Card(keyPresses);
					addCard(newCard, true);
				}
			}

			@Override
			public void complete() {
				record.remove();
			}

		});
		record.pack();

		MyUI.centerOnStage(record);
		record.toFront();
		stage.addActor(record);
	};

	void addCard(Card card, boolean scrollToBottom) {
		deckView.getAdapter().add(card);

		if (scrollToBottom) {
			deckView.rebuildView();
			deckView.getScrollPane().layout();
			deckView.getScrollPane().scrollTo(0, 0, 0, 0);
		}
	}

	@Override
	public void render(float delta) {
		MyGL.clearScreen(0f, 0f, 0f);

		// UI
		stage.act(delta);
		stage.draw();

	}
	
	// Deck loading and saving

	/**
	 * Loads the editor's deck' contents into the DeckWindow. Note that the supplied Deck will not be notified of changes
	 * 
	 * @param deck
	 */
	private void putDeckInWindow() {
		adapter.clear();
		for (Card c : deck.getHotkeys()) {
			adapter.add(c);
		}
	}
	/**
	 * Gets the decks contents from the deck window
	 */
	private void retrieveDeckFromWindow(){
		deck.getHotkeys().clear();
		for(Card c: adapter.iterable()){
			deck.add(c);
		}
	}

	/**
	 * Given a deck argument, loads the deck logically into the editor
	 */
	private void loadDeck(Deck newDeck) {
		if (newDeck != null) {
			this.deck = null;
		} else {
			System.out.println("Error: new deck was null, deck not loaded");
			return;
		}
	
		System.out.println("New deck loaded");
		this.deck = newDeck;
		newDeck.hasUnsavedChanges = false;
	
		putDeckInWindow();
	}
	/**
	 * Given a pathname, retrieves the deck from disk and loads it.
	 */
	private void loadDeckFromDisk(String pathname) {
		try {
			Deck loadedDeck = MyIO.getDeckFromDisk(pathname, app.kryo);
			loadDeck(loadedDeck);
		} catch (MyException e) {
			if (e.code == ErrorCode.IO_EXCEPTION) {
				System.out.println("IO Error, deck not loaded");
				throw e;
			}
		}
	}

	private void saveDeckToDisk(String pathname) throws IOException {		
		retrieveDeckFromWindow();
		MyIO.saveDeckToDisk(pathname, this.deck, app.kryo);
	}

	// Disk Operations
	
	private void openFileChooserThenLoadDeck() {
		System.out.println("Opening Load Dialog");
		stage.addActor(fileChooser);
	
		fileChooser.setMode(Mode.OPEN);
		fileChooser.setSelectionMode(SelectionMode.FILES);
		fileChooser.setListener(new FileChooserAdapter() {
			@Override
			public void selected(Array<FileHandle> file) {
				System.out.println("Selected file: " + file.get(0).file().getAbsolutePath());
				loadDeckFromDisk(file.get(0).file().getAbsolutePath());
			}
		});
	
		fileChooser.setDirectory(My.mapsDirectory);
	}

	private void openFileChooserThenSaveDeck() {
		System.out.println("Opening Save Dialog");
	
		fileChooser.setMode(Mode.SAVE);
		fileChooser.setSelectionMode(SelectionMode.FILES);
		fileChooser.setListener(new FileChooserAdapter() {
			@Override
			public void selected(Array<FileHandle> file) {
				System.out.println("Selected file to save to to " + file.get(0).file().getAbsolutePath());
				try {
					saveDeckToDisk(file.get(0).file().getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	
		stage.addActor(fileChooser);
		fileChooser.setDirectory(My.mapsDirectory);
	}

	@Override
	public void resize(int width, int height) {
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
	public void show() {
		System.out.println("Showed Deck Editor");
	
		multiplexer.clear();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
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
	public boolean keyDown(int keycode) {

		return false;
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

	// Disk Operations

}
