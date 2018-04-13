package com.colin29.hotkeytrainer;

import com.badlogic.gdx.Screen;
import com.colin29.hotkeytrainer.data.Deck;

/**
 * Menu that appears when you have a selected a deck. From there you can choose to edit or review the deck.
 * @author Colin Ta
 *
 */
public class DeckScreen implements Screen {
	
	
	/**
	 * A new deck screen is created every time a deck is a selected
	 */
	DeckScreen(Deck deck){
		createDeckMenu(deck);
	}
	

	private void createDeckMenu(Deck deck){
		//TODO:
	}

	@Override
	public void show() {
		System.out.println("Showed Deck Screen");
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
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
