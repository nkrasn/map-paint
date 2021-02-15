package com.krasgo.mappaint;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.krasgo.mappaint.commands.PaintMapCommand;

public class Main extends JavaPlugin implements Listener
{
	@Override
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(this, this);
		//this.data = new DataManager(this);
		this.getServer().broadcastMessage("MapPaint plugin enabled.");
		
		new PaintMapCommand(this);
	}
	
	@Override
	public void onDisable()
	{
		
	}
}
