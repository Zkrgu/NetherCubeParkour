package com.gmail.justbru00.nethercube.parkour.map;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import com.gmail.justbru00.nethercube.parkour.enums.MapDifficulty;
import com.gmail.justbru00.nethercube.parkour.enums.MapLength;
import com.gmail.justbru00.nethercube.parkour.main.NetherCubeParkour;
import com.gmail.justbru00.nethercube.parkour.utils.ItemBuilder;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import org.bukkit.inventory.meta.Damageable;

/**
 * This class handles importing maps from the config.
 * @author Justin Brubaker
 *
 */
public class MapManager {
	
	private static ArrayList<Map> maps = new ArrayList<Map>();
	
	public static void init() {	
		maps = new ArrayList<Map>();
		FileConfiguration c = NetherCubeParkour.getInstance().getConfig();
		Set<String> mapKeys = c.getConfigurationSection("maps").getKeys(false);
		
		for (String mapKey : mapKeys) {
			Map m = new Map(mapKey);
			
			String prePath = "maps." + mapKey + ".";

			try {
			String displayName = c.getString(prePath + "displayname");
			if(displayName != null) m.setDisplayName(displayName);

			String itemMaterialName = c.getString(prePath + "item.material");
			if(itemMaterialName != null) {
				try{
					Material itemMaterial = Material.valueOf(itemMaterialName);
					Integer itemData = Optional.ofNullable(c.getInt(prePath + "item.data")).orElse(0);

					ItemBuilder itemBuilder = new ItemBuilder(itemMaterial);
					itemBuilder.setDataValue(itemData);
					itemBuilder.setName(Optional.ofNullable(displayName).orElse(mapKey));

					m.setGuiItem(itemBuilder.build());
				}catch (IllegalArgumentException e){}
			}

			String difficultyName = c.getString(prePath + "difficulty");
			MapDifficulty difficulty = MapDifficulty.fromString(difficultyName);
			if(difficulty != null) m.setDifficulty(difficulty);

			String lengthName = c.getString(prePath + "length");
			MapLength length = MapLength.fromString(lengthName);
			if(length != null) m.setLength(length);
			String creatorName = c.getString(prePath + "creatorname");
			if(creatorName != null) m.setCreatorName(creatorName);

			Double startX = Optional.ofNullable(c.getDouble(prePath + "startlocation.x")).orElse(0d);
			Double startY = Optional.ofNullable(c.getDouble(prePath + "startlocation.y")).orElse(0d);
			Double startZ = Optional.ofNullable(c.getDouble(prePath + "startlocation.z")).orElse(0d);
			String startWorldName = c.getString(prePath + "startlocation.world");
			if(startWorldName != null){
				World startWorld = Bukkit.getWorld(startWorldName);
				Location startLoc = new Location(startWorld, startX, startY, startZ);
				m.setStartPlateLocation(startLoc);
			}

			Double endX = Optional.ofNullable(c.getDouble(prePath + "endlocation.x")).orElse(0d);
			Double endY = Optional.ofNullable(c.getDouble(prePath + "endlocation.y")).orElse(0d);
			Double endZ = Optional.ofNullable(c.getDouble(prePath + "endlocation.z")).orElse(0d);
			String endWorldName = c.getString(prePath + "endlocation.world");
			if(endWorldName != null){
				World endWorld = Bukkit.getWorld(endWorldName);
				Location endLoc = new Location(endWorld, endX, endY, endZ);
				m.setEndPlateLocation(endLoc);
			}

			Double spawnX = Optional.ofNullable(c.getDouble(prePath + "spawnlocation.x")).orElse(0d);
			Double spawnY = Optional.ofNullable(c.getDouble(prePath + "spawnlocation.y")).orElse(0d);
			Double spawnZ = Optional.ofNullable(c.getDouble(prePath + "spawnlocation.z")).orElse(0d);
			Float spawnYaw = Optional.ofNullable(c.getObject(prePath + "spawnlocation.yaw", Float.class)).orElse(0f);
			Float spawnPitch = Optional.ofNullable(c.getObject(prePath + "spawnlocation.pitch", Float.class)).orElse(0f);
			String spawnWorldName = c.getString(prePath + "spawnlocation.world");
			if(spawnWorldName != null){
				World spawnWorld = Bukkit.getWorld(spawnWorldName);
				Location spawnLoc = new Location(spawnWorld, spawnX, spawnY, spawnZ, spawnYaw, spawnPitch);
				m.setSpawnLocation(spawnLoc);
			}
			
			maps.add(m);
			Messager.msgConsole("&aLoaded Map " + m.getInternalName() + " by " + m.getCreatorName() + " successfully.");
			} catch (Exception e) {
				Messager.msgConsole("&cAttempt to load map " + mapKey + " FAILED. The stack trace follows this message:");
				e.printStackTrace();
			}			
		}
	}

	public static void ignoreNullptr(RunnableExc r) {
		try{ r.run(); } catch(NullPointerException e){};
	}
	@FunctionalInterface public interface RunnableExc { void run() throws NullPointerException; }
	
	public static Map getMap(String internalName) {
		for (Map map : maps) {
			if (map.getInternalName().equalsIgnoreCase(internalName)) {
				return map;
			}
		}
		return null;
	}
	
	public static ArrayList<Map> getMaps() {
		return maps;
	}

	public static ArrayList<String> getMapNames() {
		ArrayList<String> mapNames = new ArrayList<>();
		for(Map map : maps) {
			mapNames.add(map.getInternalName());
		}
		return mapNames;
	}

	public static int getNumberOfMaps() {
		return maps.size();
	}
	
	/**
	 * Searches all maps to see if one has the location registered as a plate location.
	 * @param loc The location to check.
	 * @return The map the plate belongs to, otherwise returns null.
	 */
	public static Map getMapFromPlateLocation(Location loc) {
		for (Map m : maps) {
			if (m.getStartPlateLocation().equals(loc) || m.getEndPlateLocation().equals(loc)) {
				return m;
			}
		}
		
		return null;
	}
	
	/**
	 * Searches all maps to see if one has the location registered as a plate location.
	 * @param loc The location to check.
	 * @return The MapPlateDetails of this possible plate location, otherwise returns null.
	 */
	public static MapPlateDetails getPlateDetails(Location loc) {
		for (Map m : maps) {
			Location start = m.getStartPlateLocation();
			if(start == null) break;
			if (start.equals(loc)) {
				return new MapPlateDetails(loc, true, m);
			} else if (m.getEndPlateLocation().equals(loc)) {
				return new MapPlateDetails(loc, false, m);
			}
		}
		
		return null;
	}

	public static void addMap(Map map){
		maps.add(map);
	}

	public static void saveMaps(FileConfiguration c) {
		for(Map map : maps) {
			map.save(c, "maps");
		}
		Messager.msgConsole("All maps saved");
	}

}
