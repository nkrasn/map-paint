package com.krasgo.mappaint.commands;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.krasgo.mappaint.Main;

public class PaintMapCommand implements CommandExecutor
{
	public Main plugin;
	
	public class MapImgRenderer extends MapRenderer
	{
		private BufferedImage img;
		
		public MapImgRenderer(BufferedImage img)
		{
			super();
			this.img = img;
		}
		
		@Override
		public void render(MapView view, MapCanvas canvas, Player player)
		{
			canvas.drawImage(0, 0, img);
		}
	}
	
	
	
	public PaintMapCommand(Main plugin)
	{
		this.plugin = plugin;
		plugin.getCommand("paintmap").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("This command is only for players");
			return true;
		}
		
		Player player = (Player) sender;
		ItemStack itemStack = player.getInventory().getItemInMainHand();
		//player.sendMessage("You are holding: " + itemStack.getType());
		if(!(itemStack.getType() == Material.LEGACY_MAP || itemStack.getType() == Material.FILLED_MAP))
		{
			player.sendMessage("You're not holding a map! Hold a non-empty map and try again");
			return true;
		}
		if(args.length == 0)
		{
			sender.sendMessage("You need to provide an image URL! (Discord URLs work too)");
			return true;
		}
		
		String imageUrl = args[0];
		BufferedImage img;
		
		// Read the image
		try
		{
			BufferedImage originalImg = ImageIO.read(new URL(imageUrl));
			img = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = img.createGraphics();
			g2d.drawImage(originalImg, 0, 0, 128, 128, null);
			g2d.dispose();
		}
		catch(Exception e)
		{
			player.sendMessage("Could not read the image, is your URL pointing to an image?");
			//player.sendMessage(e.getMessage());
			return true;
		}
		
		MapView map = Bukkit.createMap(this.plugin.getServer().getWorlds().get(0));
		MapMeta meta = (MapMeta) itemStack.getItemMeta();
		
		// Draw it to the canvas
		if(map != null)
		{
			map.getRenderers().clear();
			map.addRenderer(new MapImgRenderer(img));
			
			meta.setMapId(map.getId());
			itemStack.setItemMeta(meta);
			return true;
		}
		else
		{
			player.sendMessage("There was an error creating the map.");
			return true;
		}
	}
}
