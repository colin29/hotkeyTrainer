package com.colin29.hotkeytrainer;

import com.badlogic.gdx.Screen;

public class DecksMenuScreen implements Screen {
	
	DecksMenuScreen(){
		displayDecks();
	}

	/**
	 * Displays all decks in the directory. Call again to refresh the list
	 */
	public void displayDecks(){
		
		//loop through all .deck files in the directory, try to read them
		
		//create the UI for each one.
	}
	
	
	@Override
	public void show() {
		displayDecks();
	}

	@Override
	public void render(float delta) {
		System.out.println("Showed Decks Menu");
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
