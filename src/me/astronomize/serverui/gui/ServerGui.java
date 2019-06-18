package me.astronomize.serverui.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

/*
 *   _________                                ____ ___.__ 
 *  /   _____/ ______________  __ ___________|    |   \__|
 *  \_____  \_/ __ \_  __ \  \/ // __ \_  __ \    |   /  |
 *  /        \  ___/|  | \/\   /\  ___/|  | \/    |  /|  |
 * /_______  /\___  >__|    \_/  \___  >__|  |______/ |__|
 *         \/     \/                 \/       
 *  ServerGui.java
 *  
 *  Initializes the gui.
 *  
 *  functions:
 *  public static void initGui()
 *  public static void closeGui()
 *  
 *  variables:
 *  private static PluginManager manager
 *  private static JFrame serverFrame
 *  private static JPanel buttonPanel
 *  private static GridLayout layout
 *  private static JButton stopButton
 *  private static PluginDescriptionFile desc
 *  private static ArrayList<Plugin> plugins
 *  private static ArrayList<JButton> pluginButtons
 *  public static ArrayList<JButton> customPluginButtons
 *  private static Color colour_green
 *  private static Color colour_red
 *  private static Color colour_orange
 *  private static Color colour_purple
 */
public class ServerGui {
	
	private static PluginManager manager = Bukkit.getPluginManager();
	private static JFrame serverFrame;
	private static JPanel buttonPanel;
	
	private static GridLayout layout = new GridLayout(0, 4);
	
	private static JButton stopButton;
	private static JButton reloadButton;
		
	private static PluginDescriptionFile desc = manager.getPlugin("ServerUi").getDescription();
	
	private static ArrayList<Plugin> plugins;
	private static ArrayList<JButton> pluginButtons;
	public static ArrayList<JButton> customPluginButtons;
	
	private static Color colour_green = new Color(3, 204, 0);
	private static Color colour_red = new Color(244, 0, 0);
	private static Color colour_orange = new Color(255, 114, 0);
	private static Color colour_purple = new Color(217, 30, 234);
	
	public static void initGui() throws IOException {
		serverFrame = new JFrame("ServerGui - v" + desc.getVersion() + " - " + Bukkit.getServer().getName());
		
		plugins = new ArrayList<>();
		pluginButtons = new ArrayList<>();
		customPluginButtons = new ArrayList<>();
		
		buttonPanel = new JPanel();
		stopButton = new JButton("Stop Server");
		stopButton.setBorder(new RoundedBorder(10));
		stopButton.setToolTipText("Stops the Server.");
		stopButton.setBackground(colour_orange);
		
		reloadButton = new JButton("Reload Server");
		reloadButton.setBorder(new RoundedBorder(10));
		reloadButton.setToolTipText("Reloads the server.");
		reloadButton.setBackground(colour_orange);


		buttonPanel.setBackground(Color.DARK_GRAY);
		
		buttonPanel.setLayout(layout);
		
		stopButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
		    }
		});
		
		reloadButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "reload");
		    }
		});
		
		buttonPanel.add(stopButton);
		buttonPanel.add(reloadButton);
		
		// add all the plugins to the plugins list.
		for(Plugin p : manager.getPlugins()) {
			if(p.getDescription().getName() != desc.getName()) {
				plugins.add(p);
				
				Bukkit.getLogger().info("[ServerUi] Found plugin: " + p.getDescription().getName());
				
			}
		}
		for(Plugin p : plugins) {
			if(p.getDescription().getName() != "ServerUi") {
				pluginButtons.add(new JButton(p.getDescription().getName()));
				
				// force enable plugins so this:
				/*
				 * 	if(manager.getPlugin(button.getText()).isEnabled()) {
				 *  	button.setBackground(colour_green);
				 *	} else if(manager.getPlugin(button.getText()).isEnabled() == false) {
				 *		button.setBackground(colour_red);
				 *	}
				 */
				// isn't glitchy.
				manager.enablePlugin(p);
				Bukkit.getLogger().info("[ServerUi] Added plugin to list: " + p.getDescription().getName());
				
			}
		}
		
		for(JButton button : pluginButtons) {
			button.setFocusPainted(false);
		    button.setBorder(new RoundedBorder(10));
		    button.setToolTipText("Enable/Disable " + button.getText());
			buttonPanel.add(button);
			Bukkit.getLogger().info("[ServerUi] Created button for: " + button.getText());
			
			
			button.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			    	
			        // toggle whether the plugin is enabled or not
			    	if(manager.getPlugin(button.getText()).isEnabled()) {
			    		manager.disablePlugin(manager.getPlugin(button.getText()));
			    		Bukkit.getLogger().info("[ServerUi] Disabled plugin: " + button.getText());
			    		button.setBackground(colour_red);
			    	} else {
			    		manager.enablePlugin(manager.getPlugin(button.getText()));
			    		Bukkit.getLogger().info("[ServerUi] Enabled plugin: " + button.getText());
			    		button.setBackground(colour_green);
			    	}
			    }
			});
			
			if(manager.getPlugin(button.getText()).isEnabled()) {
				button.setBackground(colour_green);
			} else if(manager.getPlugin(button.getText()).isEnabled() == false) {
				button.setBackground(colour_red);
			}
		}
		
		for(JButton button : customPluginButtons) {
		    button.setFocusPainted(false);
		    button.setBorder(new RoundedBorder(10));
		    button.setBackground(colour_purple);
	            buttonPanel.add(button);
		}
		serverFrame.add(buttonPanel);
		
		serverFrame.setResizable(true);
		
		serverFrame.pack();
		serverFrame.setVisible(true);
	}
	
	public static void closeGui() {
		serverFrame.setVisible(false);
	}
	
	/**
	 * 
	 * Adds a button to the GUI.
	 */
	public static void addButton(Plugin p, JButton b) {
		customPluginButtons.add(new JButton(p.getName() + " | " + b.getText()));
	}
	
	private static class RoundedBorder implements Border {

	    private int radius;


	    RoundedBorder(int radius) {
	        this.radius = radius;
	    }


	    public Insets getBorderInsets(Component c) {
	        return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
	    }


	    public boolean isBorderOpaque() {
	        return true;
	    }


	    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	        g.drawRoundRect(x, y, width-1, height-1, radius, radius);
	    }
	}
}


