package com.colin29.hotkeytrainer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.colin29.hotkeytrainer.ReviewScreen.ReviewSettings;
import com.colin29.hotkeytrainer.data.Deck;
import com.colin29.hotkeytrainer.util.My;
import com.colin29.hotkeytrainer.util.MyGL;
import com.colin29.hotkeytrainer.util.MyUI;
import com.colin29.hotkeytrainer.util.MyUI.HeaderTable;

/**
 * Menu that appears when you have a selected a deck. From there you can choose to edit or review the deck.
 * @author Colin Ta
 *
 */

/**
 * A new deck screen is created every time a deck is a selected
 */
public class DeckScreen implements Screen {

	HotkeyApp app;

	// UI
	private Stage stage = new Stage();
	private Table root;
	private Skin skin;

	// Input
	InputMultiplexer multiplexer = new InputMultiplexer();

	// App Logic
	Deck deck;

	DeckScreen(Deck deck, HotkeyApp app) {
		this.app = app;
		this.skin = app.skin;

		this.deck = deck;

		createUI();
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
		root.add(body).fill();
		root.row().align(Align.left);
		root.add(footer);
		root.pack();

		stage.addActor(root);

		// root.setDebug(true, true);

	}

	public void createBody(Table body) {
		body.align(Align.top);
		body.row().expandX().height(150);
		body.add();
		body.row();
		Table actions = new Table().align(Align.center);
		actions.defaults().spaceRight(30).spaceBottom(30).align(Align.left).fill();
		actions.row().uniform();

		TextButtonStyle tbStyle = new TextButtonStyle(skin.get(TextButtonStyle.class));
		tbStyle.font = app.font_size1;

		MyUI.textButton(actions, "Play", skin, () -> {
			app.setScreen(new ReviewScreen(deck, app.settings, app));
		}).getActor().setStyle(tbStyle);
		MyUI.textButton(actions, "Edit", skin, () -> {
			openEditorWithThisDeck(this.deck);
		}).getActor().setStyle(tbStyle);
		actions.row();
		MyUI.textButton(actions, "Options", skin, () -> {
			openOptionsPanel();
		}).getActor().setStyle(tbStyle);

		body.add(actions);

	}
	
	

	private void openOptionsPanel(){
			final Dialog optionsDialog = new Dialog("Review options", skin){
				protected void result(Object object)
				{
				    if (object.equals(1L))
				    {
				        System.out.println("Button Pressed");
				    } else {
				        // Goto main menut
				    }
				};
			};
			Table header = optionsDialog.getTitleTable();
			Table contents = optionsDialog.getContentTable();
			Table buttons = optionsDialog.getButtonTable();

//			optionsDialog.setDebug(true, true);

			header.align(Align.center);

			// Add Property names and fields
			final CheckBox b1 = new CheckBox("Play cards in random order", skin);
			final CheckBox b2 = new CheckBox("Enable Sound", skin);
			final CheckBox b3 = new CheckBox("Avoid card repeats", skin);
			final CheckBox b4 = new CheckBox("Endless Mode", skin);
			
			contents.defaults().align(Align.left).space(10);
			contents.pad(10);
			
			contents.add(b1);
			contents.row();
			contents.add(b2);
			contents.row();
			contents.add(b3);
			contents.row();
			contents.add(b4);
			contents.row();
			
			// Synchronize buttons with current settings:
			b1.setChecked(app.settings.randomOrder);
			b2.setChecked(app.settings.soundOn);
			b3.setChecked(app.settings.avoidRepeats);
			b4.setChecked(app.settings.endlessMode);
			
			// Add Buttons
			buttons.align(Align.right);
			MyUI.textButton(buttons, "Confirm", skin,() -> {
				app.settings.randomOrder = b1.isChecked();
				app.settings.soundOn = b2.isChecked();
				app.settings.avoidRepeats = b3.isChecked();
				app.settings.endlessMode = b4.isChecked();
				optionsDialog.remove();
			});
			MyUI.textButton(buttons, "Cancel", skin, ()->{
				optionsDialog.remove();
			});
			
			optionsDialog.pack();
			MyUI.centerOnStage(optionsDialog);
			stage.addActor(optionsDialog);
			
		}

	private void openEditorWithThisDeck(Deck deck) {
		app.setScreen(app.deckEditor);
		app.deckEditor.loadDeck(deck);
	}

	@Override
	public void show() {
		System.out.println("Showed Deck Screen");
		multiplexer.clear();
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		MyGL.clearScreen(My.SECONDARY_COLOR.r, My.SECONDARY_COLOR.g, My.SECONDARY_COLOR.b);
		stage.act(delta);
		stage.draw();
		MyUI.updateTitleBar(this.deck);
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
		MyUI.clearTitleBar();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
