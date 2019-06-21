package me.astronomize.serverui;

import java.io.IOException;

import javax.swing.UnsupportedLookAndFeelException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.astronomize.serverui.exceptions.IncompatiblePluginException;
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
 *  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
 *  
 *  variables:
 *  private String[] incompatiblePluginNames
 *  private PluginDescriptionFile desc
 */
public class ServerUi extends JavaPlugin implements Listener {
	
	// the names of incompatible plugins.
	private String[] incompatiblePluginNames = new String[] {"ProtocolLib"};
	
	private PluginDescriptionFile desc = this.getDescription();
	
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(this, this);
		
		if(this.getConfig().getBoolean("auto-disable-incompatible-plugins")) {
			for(Plugin p : Bukkit.getPluginManager().getPlugins()) {
				for(String name : incompatiblePluginNames) {
					if(p.getName().equalsIgnoreCase(name)) {
						Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(name));
						try {
							
							throw new IncompatiblePluginException("[ServerUi] The plugin; " + p.getName() + " is incompatible with ServerUi."
									+ "\n Using it can possibly cause errors and other plugins to stop working.");
						} catch (IncompatiblePluginException e) {}
					}
				}
			}
		}
		
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
		ServerGui.closeGui();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("serverui")) {
			sender.sendMessage(ChatColor.GOLD + "----------------------------------------");
			sender.sendMessage(ChatColor.GREEN + "ServerUi v" + ChatColor.DARK_GREEN + desc.getVersion() + ChatColor.GREEN + " by astronomize");
			sender.sendMessage(ChatColor.GREEN + "Download link: " + ChatColor.DARK_GREEN + "https://www.spigotmc.org/resources/serverui.68429/");
			try {
				if(new SpigotUpdater(this, 68429).checkForUpdates()) {
					sender.sendMessage(ChatColor.GREEN + "New version availiable: " + ChatColor.DARK_GREEN + " Yes");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			sender.sendMessage(ChatColor.GOLD + "----------------------------------------");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("pl")) {
			String pluginList = "Plugins: ";
			for(Plugin p : Bukkit.getPluginManager().getPlugins()) {
				if(pluginList.equals("Plugins: ")) {
					if(p.isEnabled()) {
						pluginList = pluginList + ChatColor.DARK_GREEN + p.getName() + ":" + ChatColor.GREEN + " Enabled";
					} else {
						pluginList = pluginList + ChatColor.DARK_GREEN + p.getName() + ":" + ChatColor.RED + " Disabled";
					}
				} else {
					if(p.isEnabled()) {
						pluginList = pluginList + ", " + ChatColor.DARK_GREEN + p.getName() + ":" + ChatColor.GREEN + " Enabled";
					} else {
						pluginList = pluginList + ", " + ChatColor.DARK_GREEN + p.getName()  + ":" + ChatColor.RED + " Disabled";
					}
				}
			}
			sender.sendMessage(pluginList);
		}
		
		if(cmd.getName().equalsIgnoreCase("enable")) {
			if(args.length != 0) {
				if(Bukkit.getPluginManager().getPlugin(args[0]) != null) {
					Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(args[0]));
					sender.sendMessage(ChatColor.GREEN + "Enabled the plugin: " + args[0]);
				} else {
					sender.sendMessage(ChatColor.RED + "This plugin doesn't exist!");
				}
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("disable")) {
			if(args.length != 0) {
				if(Bukkit.getPluginManager().getPlugin(args[0]) != null) {
					Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(args[0]));
					sender.sendMessage(ChatColor.GREEN + "Disabled the plugin: " + args[0]);
				} else {
					sender.sendMessage(ChatColor.RED + "This plugin doesn't exist!");
				}
			}
		}
		return false;
	}
	
}
