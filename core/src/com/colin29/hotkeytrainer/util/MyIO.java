package com.colin29.hotkeytrainer.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.colin29.hotkeytrainer.data.Deck;
import com.colin29.hotkeytrainer.util.exception.ErrorCode;
import com.colin29.hotkeytrainer.util.exception.MyException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.Input;

public class MyIO {
	public static Deck getDeckFromDisk(String pathname, Kryo kryo) {

		Input input;
		try {
			input = new Input(new FileInputStream(pathname));
		} catch (FileNotFoundException e) {
			System.out.println("IO Exception while loading Deck");
			e.printStackTrace();
			throw new MyException(ErrorCode.IO_EXCEPTION);
		}
		
		Deck deck;
		try {
			deck = kryo.readObject(input, Deck.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyException(ErrorCode.IO_EXCEPTION);
		} finally {
			input.close();
		}
//		System.out.printf("Deck size: %s\n", deck.getHotkeys().size);
		return deck;
	}

	public static void saveDeckToDisk(String pathname, Deck deck, Kryo kryo) {

		Output output;
		try {
			output = new Output(new FileOutputStream(pathname));
		} catch (FileNotFoundException e1) {
			throw new MyException(ErrorCode.IO_EXCEPTION);
		}
		try {

			kryo.writeObject(output, deck);
			output.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new MyException(ErrorCode.IO_EXCEPTION);
		} finally {
			output.close();
		}
		deck.hasUnsavedChanges = false;
		System.out.println("Writing Deck to disk finished");
	}

	public static void registerSerializers(Kryo kryo) {
		kryo.register(Array.class, new Serializer<Array>() {
			{
				setAcceptsNull(true);
			}

			private Class genericType;

			public void setGenerics(Kryo kryo, Class[] generics) {
				if (generics != null && kryo.isFinal(generics[0]))
					genericType = generics[0];
				else
					genericType = null;
			}

			public void write(Kryo kryo, Output output, Array array) {
				int length = array.size;
				output.writeInt(length, true);
				if (length == 0) {
					genericType = null;
					return;
				}
				if (genericType != null) {
					Serializer serializer = kryo.getSerializer(genericType);
					genericType = null;
					for (Object element : array)
						kryo.writeObjectOrNull(output, element, serializer);
				} else {
					for (Object element : array)
						kryo.writeClassAndObject(output, element);
				}
			}

			public Array read(Kryo kryo, Input input, Class<Array> type) {
				Array array = new Array();
				kryo.reference(array);
				int length = input.readInt(true);
				array.ensureCapacity(length);
				if (genericType != null) {
					Class elementClass = genericType;
					Serializer serializer = kryo.getSerializer(genericType);
					genericType = null;
					for (int i = 0; i < length; i++)
						array.add(kryo.readObjectOrNull(input, elementClass, serializer));
				} else {
					for (int i = 0; i < length; i++)
						array.add(kryo.readClassAndObject(input));
				}
				return array;
			}
		});

		kryo.register(Color.class, new Serializer<Color>() {
			public Color read(Kryo kryo, Input input, Class<Color> type) {
				Color color = new Color();
				Color.rgba8888ToColor(color, input.readInt());
				return color;
			}

			public void write(Kryo kryo, Output output, Color color) {
				output.writeInt(Color.rgba8888(color));
			}
		});
	}

	// utility functions
	public static String getCanonicalPath(String string) {
		try {
			return Paths.get(string).toRealPath().toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
