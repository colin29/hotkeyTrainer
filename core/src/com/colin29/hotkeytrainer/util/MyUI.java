package com.colin29.hotkeytrainer.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MyUI {

	public interface VoidInterface {
		public void run();
	}
	public interface FloatConsumer {
		public void accept(Float f);
	}


	// Widget Functions
	
	/**
	 * Creates a text button and adds it to the table given.
	 */
	public static Cell<TextButton> textButton(Table table, String text, Skin skin, VoidInterface action) {
		return textButton(table, text, skin, new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				action.run();
			}
		});
	}
	

	private static Cell<TextButton> textButton(Table table, String text, Skin skin, EventListener listener) {
		TextButton button = new TextButton(text, skin);
		button.addListener(listener);
		return table.add(button);
	}

	/**
	 * Creates an exit button
	 * 
	 * @param table
	 *            Location to add the button
	 * @param skin
	 * @param parent
	 *            UI element which the button should close
	 * @return The enclosing cell for the new button
	 */
	public static Cell<TextButton> exitButton(Table table, Skin skin, final Actor parent) {
		return textButton(table, "x", skin, new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.remove();
			}
		});
	}

	public static void confirmationDialog(boolean condition, Stage stage, Skin skin, String titleText,
			String contentText, String doAlteredText, String doOriginalText, VoidInterface alteredAction,
			VoidInterface originalAction) {

		if (condition) {
			Dialog dialog = new Dialog(titleText, skin);
			stage.addActor(dialog);

			Table contents = dialog.getContentTable();
			contents.add(new Label(contentText, skin));

			Table buttons = dialog.getButtonTable();
			textButton(buttons, doAlteredText, skin, () -> {
				alteredAction.run();
				dialog.remove();
			});
			textButton(buttons, doOriginalText, skin, () -> {
				originalAction.run();
				dialog.remove();
			});
			textButton(buttons, "Cancel", skin, () -> {
				dialog.remove();
			});
			dialog.pack();
			centerOnStage(dialog);
		} else {
			originalAction.run();
		}

	}
	/**
	 * Creates a parameter slider for quickly adjusting the values of parameters
	 * @param action This should be a lambda which takes in a float and sets the parameter
	 */
	public static void parameterSlider(float minVal, float maxVal, String parameterName, Table parent, Skin skin, FloatConsumer action){
		Label vLabel = new Label("-", skin);
		Label vName = new Label(parameterName, skin);
		Slider vSlider = new Slider(minVal, maxVal, (maxVal-minVal)/30, false, skin);
		vSlider.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				action.accept(vSlider.getValue());
				vLabel.setText(String.valueOf(vSlider.getValue()));
			}
		});
		parent.add();
		parent.add(vSlider);
		parent.row();
		parent.add(vName, vLabel);
		parent.row();
	}

	public static void centerOnStage(Actor actor) {
		actor.setPosition(Gdx.graphics.getWidth() / 2 - (int) actor.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - (int) actor.getHeight() / 2);
	}
	
	public static void printDimensions(String tableName, Table table){
		System.out.printf("Table %s has dimensions (%s,%s)\n", tableName, table.getRows(), table.getColumns());
	}
	public static Color visSkinColor(){
		return new Color(0x353535ff);
	}
	
	public static class HeaderTable extends Table{
		public HeaderTable(Skin skin){
			
			row().space(0, My.optionButtonSpacing, 0, My.optionButtonSpacing);
			
			decksButton = MyUI.textButton(this, "Decks", skin, () -> {}).getActor();
			editButton = MyUI.textButton(this, "Edit", skin, () -> {}).getActor();
			pack();
			
			setBackground(MyGL.getBGDrawable(this, new Color(0x888888FF))); //(0x555555FF)
		}
		
		public TextButton decksButton;
		public TextButton editButton;
	}
}
