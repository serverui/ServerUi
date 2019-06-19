package me.astronomize.serverui;

import java.io.IOException;

import javax.swing.UnsupportedLookAndFeelException;

import org.bukkit.plugin.java.JavaPlugin;

import me.astronomize.serverui.gui.ServerGui;
import me.astronomize.serverui.updater.SpigotUpdater;;

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
		
	    SpigotUpdater updater = new SpigotUpdater(this, 68429);
	    try {
	        if (updater.checkForUpdates()) {
	            getLogger().info("An update was found! New version: " + updater.getLatestVersion() + "! download it here: " + updater.getResourceURL());
	        }
	    } catch (Exception e) {
	        getLogger().severe("Could not check for updates! Stacktrace:");
	        e.printStackTrace();
	    }
		
		this.saveDefaultConfig();
		getLogger().info("Saved plugin configuration.");
	    
		// opens and creates the GUI
		try {
			ServerGui.initGui();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public void onDisable() {
		
		// closes the GUI
		ServerGui.closeGui();
	}
	
}
