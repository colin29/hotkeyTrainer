package com.colin29.hotkeytrainer;

import java.awt.event.ActionListener;
import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.colin29.hotkeytrainer.data.Card;
import com.colin29.hotkeytrainer.data.Deck;
import com.colin29.hotkeytrainer.data.KeyPress;
import com.colin29.hotkeytrainer.util.My;
import com.colin29.hotkeytrainer.util.MyGL;
import com.colin29.hotkeytrainer.util.MyIO;
import com.colin29.hotkeytrainer.util.MyUI;
import com.colin29.hotkeytrainer.util.MyUI.HeaderTable;
import com.kotcrab.vis.ui.widget.VisScrollPane;

/**
 * Screen where users can see all their decks at a glance
 * 
 * @author Colin Ta
 *
 */
public class DecksMenuScreen implements Screen {

	HotkeyApp app;

	// UI
	private Stage stage = new Stage();
	private Table root;
	private Skin skin;

	// Input
	InputMultiplexer multiplexer = new InputMultiplexer();

	DecksMenuScreen(HotkeyApp app) {
		this.app = app;
		this.skin = app.skin;

		createUI();
	}

	/**
	 * Displays all decks in the directory. Call again to refresh the list
	 */
	public void rebuildDeckTable() {

		// loop through all .deck files in the directory, try to read them
		Array<Deck> decks = getAllDecksInDirectory();

		deckTable.defaults().spaceLeft(20);

		// create the UI for each one.
		deckTable.clear();
		
		for(Deck deck: decks){
			deckTable.row();
			
			LabelStyle labelStyle = new Label.LabelStyle(app.font_size2, Color.BLACK);
			String name = deck.getName() == "" ? "Untitled Deck" :
				deck.getName();
			Label deckName = new Label(name, labelStyle);
			deckName.addListener(new ClickListener(){
				@Override
				public void clicked (InputEvent event, float x, float y) {
//					openEditorWithThisDeck(deck);
					
					 DeckScreen deckScreen = new DeckScreen(deck, app);
					 app.setScreen(deckScreen);
				}
			});
			deckName.setTouchable(Touchable.enabled);
			
			deckTable.add(deckName);
			deckTable.add(new Label("(" + deck.getHotkeys().size + " cards)", labelStyle));	
		}		
		deckTable.pack();
	}
	


	private Array<Deck> getAllDecksInDirectory() {
		Array<String> results = new Array<String>();

		File[] files = new File("./").listFiles();
		// If this pathname does not denote a directory, then listFiles() returns null.

		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();
				if(fileName.endsWith(".deck")){
					results.add(file.getName());
				}
			}
		}

		System.out.println(results.toString());
		
		Array<Deck> decks = new Array<Deck>();
		
		for(String deckPath: results){
			Deck deck = MyIO.getDeckFromDisk(My.mapsDirectory + "/" + deckPath, app.kryo);
			deck.diskPath = My.mapsDirectory + "/" + deckPath;
			decks.add(deck);
		}
		return decks;

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

//		root.setDebug(true, true);

		stage.addActor(root);

	}

	private Table deckTable;

	public void createBody(Table body) {
		body.pad(20);
		deckTable = new Table().pad(20);
		ScrollPane deckDisplay = new ScrollPane(deckTable, skin);
		body.add(deckDisplay);

	}

	@Override
	public void show() {
		System.out.println("Showed Decks Menu");
		multiplexer.clear();
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);

		rebuildDeckTable();

	}

	@Override
	public void render(float delta) {
		MyGL.clearScreen(My.SECONDARY_COLOR.r, My.SECONDARY_COLOR.g, My.SECONDARY_COLOR.b);
		stage.act(delta);
		stage.draw();
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

}
