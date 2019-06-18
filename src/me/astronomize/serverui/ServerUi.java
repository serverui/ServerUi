package me.astronomize.serverui;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import me.astronomize.serverui.gui.Init;;

/*
 *   _________                                ____ ___.__ 
 *  /   _____/ ______________  __ ___________|    |   \__|
 *  \_____  \_/ __ \_  __ \  \/ // __ \_  __ \    |   /  |
 *  /        \  ___/|  | \/\   /\  ___/|  | \/    |  /|  |
 * /_______  /\___  >__|    \_/  \___  >__|  |______/ |__|
 *         \/     \/                 \/       
 *         
 *  ServerUi.java
 *  
 *  the main plugin class
 *  
 *  functions:
 *  public void onEnable()
 *  public void onDisable()
 */
public class ServerUi extends JavaPlugin {
	
	public void onEnable() {
		
		// opens and creates the GUI
		try {
			Init.initGui();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onDisable() {
		
		// closes the GUI
		Init.closeGui();
	}
	
}
