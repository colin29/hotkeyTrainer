package com.colin29.hotkeytrainer.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

import com.colin29.hotkeytrainer.Deck;
import com.colin29.hotkeytrainer.util.exception.ErrorCode;
import com.colin29.hotkeytrainer.util.exception.MyException;

public class MyIO {
	public static Deck getDeckFromDisk(String pathname) {
		Deck deck;
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		try {
			fileIn = new FileInputStream(pathname);
			in = new ObjectInputStream(fileIn);
			deck = (Deck) in.readObject();
			return deck;

		} catch (IOException i) {
			System.out.println("IO Exception while loading Deck");
			i.printStackTrace();
			throw new MyException(ErrorCode.IO_EXCEPTION);
		} catch (ClassNotFoundException c) {
			System.out.println("Field class not found");
			c.printStackTrace();
			throw new MyException(ErrorCode.IO_EXCEPTION);
		} finally {
			try {
				in.close();
				fileIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void saveDeckToDisk(String pathname, Deck deck) {
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		try {

			fout = new FileOutputStream(pathname);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(deck);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					fout.close();
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		deck.hasUnsavedChanges = false;
		System.out.println("Writing Map to disk finished");
	}

	//utility functions
	public static String getCanonicalPath(String string){
		try {
			return Paths.get(string).toRealPath().toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
