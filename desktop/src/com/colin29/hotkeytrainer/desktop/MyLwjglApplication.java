package com.colin29.hotkeytrainer.desktop;

import java.lang.Thread.UncaughtExceptionHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.colin29.hotkeytrainer.util.exception.StackTraceAppender;

public class MyLwjglApplication extends LwjglApplication{

	public MyLwjglApplication(ApplicationListener listener) {
		super(listener);
		setExceptionHandler();
	}
	public MyLwjglApplication(ApplicationListener listener, LwjglApplicationConfiguration config) {
		super(listener, config);
		setExceptionHandler();
	}
	
	void setExceptionHandler(){
		super.mainLoopThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler(){

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				StackTraceAppender.appendToFile(e);
				System.out.println("Exception logged to disk");
				e.printStackTrace(System.out);
				throw new RuntimeException();
			}
			
		});
	}


}
