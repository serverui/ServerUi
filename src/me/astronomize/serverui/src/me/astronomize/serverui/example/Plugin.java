package me.astronomize.serverui.example;

import javax.swing.JButton;

import org.bukkit.plugin.java.JavaPlugin;

import me.astronomize.serverui.gui.ServerGui;

public class Plugin extends JavaPlugin {

	public void onEnable() {
		ServerGui.addButton(this, new JButton("Example Button!"));
	}
}
