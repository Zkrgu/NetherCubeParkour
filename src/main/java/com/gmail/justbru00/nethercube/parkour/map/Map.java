package com.gmail.justbru00.nethercube.parkour.map;

import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.nethercube.parkour.enums.MapDifficulty;
import com.gmail.justbru00.nethercube.parkour.enums.MapLength;
import org.bukkit.inventory.meta.Damageable;

public class Map {
	
	public static final int UNLOCKED_DEFAULT = -1;

	private String internalName;
	private String displayName;
	private ItemStack guiItem;
	private MapDifficulty difficulty;
	private MapLength length;
	private String creatorName;
	private Location startPlateLocation;
	private Location endPlateLocation;
	private Location spawnLocation;
	
	public Map(String internalName) {
		this.internalName = internalName;
	}

	/**
	 * Gets the spawnpoint for this map
	 * @return
	 */
	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	public String getInternalName() {
		return internalName;
	}
	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public ItemStack getGuiItem() {
		return guiItem;
	}
	public void setGuiItem(ItemStack guiItem) {
		this.guiItem = guiItem;
	}
	public MapDifficulty getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(MapDifficulty difficulty) {
		this.difficulty = difficulty;
	}
	public MapLength getLength() {
		return length;
	}
	public void setLength(MapLength length) {
		this.length = length;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Location getStartPlateLocation() {
		return startPlateLocation;
	}
	public void setStartPlateLocation(Location startPlateLocation) {
		this.startPlateLocation = startPlateLocation;
	}
	public Location getEndPlateLocation() {
		return endPlateLocation;
	}
	public void setEndPlateLocation(Location endingPlateLocation) {
		this.endPlateLocation = endingPlateLocation;
	}

	public void save(FileConfiguration c, String root){
		String basePath = root + "." + internalName + ".";

		if(displayName != null) c.set(basePath + "displayname", displayName);

		if(guiItem != null) {
			c.set(basePath + "item.material", guiItem.getType().toString());
			c.set(basePath + "item.data", ((Damageable) guiItem.getItemMeta()).getDamage());
		}

		if(difficulty != null) c.set(basePath + "difficulty", difficulty.toString());
		if(length != null) c.set(basePath + "length", length.toString());
		if(creatorName != null) c.set(basePath + "creatorname", creatorName);

		if(startPlateLocation != null) {
			c.set(basePath + "startlocation.x", startPlateLocation.getBlockX());
			c.set(basePath + "startlocation.y", startPlateLocation.getBlockY());
			c.set(basePath + "startlocation.z", startPlateLocation.getBlockZ());
			c.set(basePath + "startlocation.world", startPlateLocation.getWorld().getName());
		}

		if(endPlateLocation != null) {
			c.set(basePath + "endlocation.x", endPlateLocation.getBlockX());
			c.set(basePath + "endlocation.y", endPlateLocation.getBlockY());
			c.set(basePath + "endlocation.z", endPlateLocation.getBlockZ());
			c.set(basePath + "endlocation.world", endPlateLocation.getWorld().getName());
		}

		if(spawnLocation != null) {
			c.set(basePath + "spawnlocation.x", spawnLocation.getX());
			c.set(basePath + "spawnlocation.y", spawnLocation.getY());
			c.set(basePath + "spawnlocation.z", spawnLocation.getZ());
			c.set(basePath + "spawnlocation.yaw", spawnLocation.getYaw());
			c.set(basePath + "spawnlocation.pitch", spawnLocation.getPitch());
			c.set(basePath + "spawnlocation.world", spawnLocation.getWorld().getName());
		}

		Messager.msgConsole("Saved " + internalName);
	}
}
