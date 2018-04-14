package com.colin29.hotkeytrainer;

import java.util.Comparator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.colin29.hotkeytrainer.HotkeyTrainer.KeyModifier;
import com.colin29.hotkeytrainer.data.Card;
import com.colin29.hotkeytrainer.data.Deck;
import com.colin29.hotkeytrainer.data.KeyPress;
import com.colin29.hotkeytrainer.util.MyGL;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.adapter.AbstractListAdapter.SelectionMode;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.ListView.UpdatePolicy;

public class DeckWindow extends VisWindow {

	private ListView<Card> view;
	DeckAdapter adapter;

	public DeckWindow() {
		super("Deck:");
		TableUtils.setSpacingDefaults(this);
		//MyGL.setBgColor(super.getTitleLabel(), Color.PINK);
		columnDefaults(0).left();

		Array<Card> array = new Array<Card>();
		adapter = new DeckAdapter(array);
		
		view = new ListView<Card>(adapter);
		view.setUpdatePolicy(UpdatePolicy.ON_DRAW);
		
		row();
		add(view.getMainTable()).grow();
		
		
		
	}
	public ArrayAdapter<Card, VisTable> getRemoveAdapter(){
		return adapter;
	}

	public ListView<Card> getDeckView(){
		return view;
	}
	
	public VisTable getListViewTable() {
		return view.getMainTable();
	}

	private static class DeckAdapter extends ArrayAdapter<Card, VisTable> {
		private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
		private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");

		public DeckAdapter(Array<Card> array) {
			super(array);
			setSelectionMode(SelectionMode.MULTIPLE);

		}

		@Override
		protected VisTable createView(Card card) { // Creates a view for a single item
			VisLabel label = new VisLabel(card.getKeyPressesAsText());
			label.setColor(Color.WHITE);

			VisTable table = new VisTable();
			table.left();
			table.add(label);
			return table;
		}

		@Override
		protected void selectView(VisTable view) {
			view.setBackground(selection);
		}

		@Override
		protected void deselectView(VisTable view) {
			view.setBackground(bg);
		}
	}

}
