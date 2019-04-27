package com.oop.platformer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.oop.platformer.GameClass;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 940;
		config.height = 710;

// 		java - get screen size using the Toolkit class
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		config.width = (int)screenSize.getWidth();
//		config.height = (int)screenSize.getHeight();
//		config.backgroundFPS = 60;
//		config.foregroundFPS = 60;
//		config.resizable = false;
//		config.fullscreen = true;

		new LwjglApplication(new GameClass(), config);
	}
}
