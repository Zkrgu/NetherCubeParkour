package com.gmail.justbru00.nethercube.parkour.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.nethercube.parkour.gui.GUIManager;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;
import com.gmail.justbru00.nethercube.parkour.timer.PlayerTimer;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;

public class MainGUIListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		if (e.getInventory() != null) {
			if (e.getView().getTitle() != null) {
				if (e.getView().getTitle().startsWith(Messager.color("&6Courses: "))) {
					// Is the main GUI 
					e.setCancelled(true);
					
					if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
						ItemStack item = e.getCurrentItem();				
						
						if (item.getItemMeta() == null || item.getItemMeta().getLore() == null)  {
							// Item doesn't have lore
							return;
						}
						
						List<String> lore = item.getItemMeta().getLore();

							// Map is unlocked - Teleport them to the start
						if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {
								// Ensure that the player is not in a map
							PlayerTimer.playerLeavingMap((Player) e.getWhoClicked(), false);
							String mapName = ChatColor.stripColor(item.getItemMeta().getLore().get(4));

							Location spawnLocation = MapManager.getMap(mapName).getSpawnLocation();
							if(spawnLocation != null) e.getWhoClicked().teleport(spawnLocation, TeleportCause.PLUGIN);
							else Messager.msgPlayer("Map does not have a spawn point", (Player) e.getWhoClicked());
						}
					}
				}
			}
		}
		
	}
	
}
