package com.gmail.justbru00.nethercube.parkour.map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.nethercube.parkour.enums.MapDifficulty;
import com.gmail.justbru00.nethercube.parkour.enums.MapLength;

public class Map {
	
	public static final int UNLOCKED_DEFAULT = -1;

	private String internalName;
	private ItemStack guiItem;
	private MapDifficulty difficulty;
	private MapLength length;
	private String creatorName;
	private int rewardAmount;
	private Location startPlateLocation;
	private Location endingPlateLocation;
	private Location spawnLocation;
	
	public Map(String internalName) {
		this.internalName = internalName;
	}
	
	public Map(String internalName, ItemStack guiItem, MapDifficulty difficulty, MapLength length, String creatorName,
			int rewardAmount, Location startPlateLocation, Location endingPlateLocation) {
		super();
		this.internalName = internalName;
		this.guiItem = guiItem;
		this.difficulty = difficulty;
		this.length = length;
		this.creatorName = creatorName;
		this.rewardAmount = rewardAmount;
		this.startPlateLocation = startPlateLocation;
		this.endingPlateLocation = endingPlateLocation;
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
	public int getRewardAmount() {
		return rewardAmount;
	}
	public void setRewardAmount(int rewardAmount) {
		this.rewardAmount = rewardAmount;
	}
	public Location getStartPlateLocation() {
		return startPlateLocation;
	}
	public void setStartPlateLocation(Location startPlateLocation) {
		this.startPlateLocation = startPlateLocation;
	}
	public Location getEndingPlateLocation() {
		return endingPlateLocation;
	}
	public void setEndingPlateLocation(Location endingPlateLocation) {
		this.endingPlateLocation = endingPlateLocation;
	}
	
	
	
}
