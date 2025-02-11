package com.gmail.justbru00.nethercube.parkour.gui;


import com.gmail.justbru00.nethercube.parkour.data.PlayerData;
import com.gmail.justbru00.nethercube.parkour.data.PlayerMapData;
import com.gmail.justbru00.nethercube.parkour.map.Map;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;
import com.gmail.justbru00.nethercube.parkour.utils.ItemBuilder;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {

	private static ItemStack borderGlass;
	private static ItemStack okay;
	private static ItemStack cancel;
	private static ItemStack cannotAffordMap;
	
	public static void init() {
		
		borderGlass = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("&r").build();
		okay = new ItemBuilder(Material.EMERALD_BLOCK).setName("&a&lOkay").build();
		cancel = new ItemBuilder(Material.REDSTONE_BLOCK).setName("&c&lCancel").build();
		cannotAffordMap = new ItemBuilder(Material.BARRIER).setName("&c&lYou can't afford that map").build();
	}
	
	public static ItemStack getBorderGlass() {
		return borderGlass;
	}
	
	public static ItemStack getOkay() {
		return okay;
	}
	
	public static ItemStack getCancel() {
		return cancel;
	}
	
	public static ItemStack getCannotAfford() {
		return cannotAffordMap;
	}
	/**
	 * Opens the confirm purchase gui
	 * @param p
	 * @param mapItem
	 */
	public static void openConfirmGUI(Player p, ItemStack mapItem) {
		
		Inventory inv = Bukkit.createInventory(null, 54, Messager.color("&6Confirm Purchase"));
		
		// Set the border glass
		// 0-9, 10,18,19,27,28,36,37,45,46-54 minus one for all these
				
		Integer[] borderSlots = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};
				
		for (Integer slot : borderSlots) {
			inv.setItem(slot, borderGlass);
		}
		
		// Okay items
		
		Integer[] okaySlots = {15,16,24,25,33,34,42,43};
		
		for (Integer slot : okaySlots) {
			inv.setItem(slot, okay);
		}
		
		// Cancel items
		
		Integer[] cancelSlots = {10,11,19,20,28,29,37,38};
		
		for (Integer slot : cancelSlots) {
			inv.setItem(slot, cancel);
		}
		
		// Map clicked item in center
		
		ItemMeta im = mapItem.getItemMeta();
		List<String> lore = im.getLore();
		String priceLine = lore.get(0);
		priceLine = ChatColor.stripColor(priceLine);
		priceLine = priceLine.substring(25).trim();
		int price = Integer.parseInt(priceLine);
		lore.set(0, Messager.color("&7Cost: &e" + price));
		
		im.setLore(lore);
		
		mapItem.setItemMeta(im);
		
		inv.setItem(22, mapItem);
		
		p.openInventory(inv);
		
	}
	
	/**
	 * Opens the main GUI for map selection, unlocking, and instant teleporting.
	 * Players can left click to teleport to the start maps
	 * Players can right click to purchase locked maps
	 * @param p
	 */
	public static void openMainGUI(Player p) {
		
		//PlayerData pd = PlayerData.getDataFor(p);
		
		//Inventory inv = Bukkit.createInventory(null, 54, Messager.color("&6Currency: " + pd.getCurrency()));
		Inventory inv = Bukkit.createInventory(null, 54, Messager.color("&6Courses: "));
		
		// Set the border glass
		// 0-9, 10,18,19,27,28,36,37,45,46-54 minus one for all these
		
		Integer[] borderSlots = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};
		
		for (Integer slot : borderSlots) {
			inv.setItem(slot, borderGlass);
		}
		
		// Fill the maps in
		for (Map map : MapManager.getMaps()) {
			inv.addItem(getMapItemFor(p, map.getInternalName()));
		}
		
		p.openInventory(inv);		
	}
	
	/**
	 * Fills the lore for each map icon for the specified player.
	 * @param p
	 * @param mapInternalName
	 * @return
	 */
	private static ItemStack getMapItemFor(Player p, String mapInternalName) {
		
		PlayerData pd = PlayerData.getDataFor(p.getUniqueId());
		PlayerMapData pmd = pd.getMapData(mapInternalName);
		Map map = MapManager.getMap(mapInternalName);
		
		ItemStack toReturn = map.getGuiItem().clone();
		
		List<String> loreToSet = new ArrayList<String>();
		
		// First line - Unlock status and price if not unlocked
		String firstLine;
		firstLine = Messager.color("&a&lLeft click to start");
		// End First Line
		
		// Third Line - Players best time for this map
		String secondLine;
		
		Messager.debug("Best time is: " + pmd.getBestTime());
		if (pmd.getBestTime() == PlayerMapData.NO_BEST_TIME) {
			secondLine = Messager.color("&7Your best time: &eNever Finished");
		} else {
			secondLine = Messager.color("&7Your best time: &e" + Messager.formatAsTime(pmd.getBestTime()));
		}
		
		// End Third Line
		
		// Fourth Line - Times Finished for the player
		String thirdLine = Messager.color("&7Times finished: &e" + pmd.getFinishes());
		// End fourth line
		
		// Fifth line
		String fourthLine = Messager.color("&7Attempts: &e" + pmd.getAttempts());
		// End fourthLine
		
		// Sixth Line - This maps internal name. Important for the Confirm GUI.
		String fifthLine = Messager.color("&7" + map.getInternalName());
		// End Sixth line
		
		loreToSet.add(firstLine);
		loreToSet.add(secondLine);
		loreToSet.add(thirdLine);
		loreToSet.add(fourthLine);
		loreToSet.add(fifthLine);

		Messager.debug("Map is unlocked | GLOWING");
		if (toReturn.getType() == Material.FISHING_ROD) {
			Messager.debug("ITEM IS FISHING ROD");
			toReturn.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 4341);
			ItemMeta meta = toReturn.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			toReturn.setItemMeta(meta);
		} else {
			Messager.debug("ITEM IS NOT FISHING ROD");
			toReturn.addUnsafeEnchantment(Enchantment.LURE, 4341);
			ItemMeta meta = toReturn.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			toReturn.setItemMeta(meta);
		}
		
		ItemMeta im = toReturn.getItemMeta();		
		im.setLore(loreToSet);
		toReturn.setItemMeta(im);
		
		return toReturn;
	}
	
}
