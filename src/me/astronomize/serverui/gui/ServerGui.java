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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

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
 *  private static JButton reloadButton
 *  private static JButton loadButton
 *  private static PluginDescriptionFile desc
 *  private static FileConfiguration config
 *  private static ArrayList<Plugin> plugins
 *  private static ArrayList<JButton> pluginButtons
 *  private static ArrayList<JButton> customPluginButtons
 *  private static ArrayList<JButton> customCommandButtons;
 *  private static Color colour_green
 *  private static Color colour_red
 *  private static Color colour_orange
 *  private static Color colour_purple
 *  private static Color colour_teal
 */
public class ServerGui {
	
	private static PluginManager manager = Bukkit.getPluginManager();
	private static JFrame serverFrame;
	private static JPanel buttonPanel;
	
	private static GridLayout layout = new GridLayout(0, 4);
	
	private static JButton stopButton;
	private static JButton reloadButton;
	private static JButton loadButton;
			
	private static PluginDescriptionFile desc = manager.getPlugin("ServerUi").getDescription();
	private static FileConfiguration config;
	
	private static ArrayList<Plugin> plugins;
	private static ArrayList<JButton> pluginButtons;
	private static ArrayList<JButton> customPluginButtons;
	private static ArrayList<JButton> customCommandButtons;
	
	// the colour used for enabled plugin buttons.
	private static Color colour_green = new Color(3, 204, 0);
	
	// the colour used for disabled plugin buttons.
	private static Color colour_red = new Color(244, 0, 0);
	
	// the colour used for server buttons(stop, reload, etc)
	private static Color colour_orange = new Color(255, 114, 0);
	
	// the colour used for plugin API buttons.
	private static Color colour_purple = new Color(217, 30, 234);
	
	// the colour used for custom command buttons.
	private static Color colour_teal = new Color(0, 250, 255);
	
	public static void initGui() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		config = manager.getPlugin("ServerUi").getConfig();
		List<String> commandButtons = config.getStringList("command-buttons");
		serverFrame = new JFrame("ServerGui - v" + desc.getVersion() + " - " + Bukkit.getServer().getName());
		
		plugins = new ArrayList<>();
		pluginButtons = new ArrayList<>();
		customPluginButtons = new ArrayList<>();
		customCommandButtons = new ArrayList<>();
		
		buttonPanel = new JPanel();
		
		RoundedBorder border = new RoundedBorder(10);
		
		loadButton = new JButton("Load Plugin");
		loadButton.setToolTipText("Stops the Server.");
		loadButton.setBorder(border);
		loadButton.setBackground(colour_orange);

		
		stopButton = new JButton("Stop Server");
		stopButton.setToolTipText("Stops the Server.");
		stopButton.setBorder(border);
		stopButton.setBackground(colour_orange);
		
		reloadButton = new JButton("Reload Server");
		reloadButton.setToolTipText("Reloads the server.");
		reloadButton.setBorder(border);
		reloadButton.setBackground(colour_orange);

		buttonPanel.setBackground(Color.DARK_GRAY);
		
		buttonPanel.setLayout(layout);
		
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("Jar Files", "jar");
			    chooser.setFileFilter(filter);
			    int ret = chooser.showOpenDialog(chooser);
			    if(ret == JFileChooser.APPROVE_OPTION) {
			    	try {
						manager.loadPlugin(chooser.getSelectedFile());
					} catch (UnknownDependencyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidPluginException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidDescriptionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}
			
		});
		
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
		buttonPanel.add(loadButton);
		
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
		    button.setBorder(border);
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
		    button.setBorder(border);
			button.setBackground(colour_purple);
			buttonPanel.add(button);
		}
		
		for(String s : commandButtons) {
			Bukkit.getLogger().info("[ServerUi] " + s);
			if(s.contains("/")) {
				customCommandButtons.add(new JButton(s));
			} else {
				customCommandButtons.add(new JButton("/" + s));
			}
		}
		
		for(JButton button : customCommandButtons) {
			button.setFocusPainted(false);
		    button.setBorder(border);
			button.setBackground(colour_teal);
			button.setToolTipText("Runs the command: " + button.getText());
			button.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			    	
			    	Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), button.getText().replaceAll("/", ""));
			    	
			    }
			});
			
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


