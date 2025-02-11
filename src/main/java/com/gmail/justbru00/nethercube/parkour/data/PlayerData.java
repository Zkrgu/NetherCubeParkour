package com.gmail.justbru00.nethercube.parkour.data;

import com.gmail.justbru00.nethercube.parkour.map.Map;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static com.gmail.justbru00.nethercube.parkour.main.NetherCubeParkour.dataFile;

public class PlayerData {

	private UUID uuid;
	private ArrayList<PlayerMapData> mapData = new ArrayList<PlayerMapData>();
	
	public static PlayerData getDataFor(UUID id) {
		PlayerData pd = new PlayerData(id);
		
		if (dataFile.getConfigurationSection(id.toString()) == null) {
			// Make the section as it does not exist.
			String pathPre = id.toString() + ".";

			pathPre = id.toString() + ".maps.";
			
			for (Map map : MapManager.getMaps()) {
				dataFile.set(pathPre + map.getInternalName() + ".attempts", 0);
				dataFile.set(pathPre + map.getInternalName() + ".finishes", 0);
				dataFile.set(pathPre + map.getInternalName() + ".besttime", (long) -1);
			}	
			dataFile.save();
		}
		
		// Get all data from config.
		
		String prePath = id.toString() + ".maps.";
		
		Set<String> mapsFromData = dataFile.getConfigurationSection(id.toString() + ".maps").getKeys(false);
		
		ArrayList<PlayerMapData> playerMapData = new ArrayList<PlayerMapData>();
		
		for (Map map : MapManager.getMaps()) {
			if (mapsFromData.contains(map.getInternalName())) {
				// Get data for this map
				PlayerMapData mapData = new PlayerMapData(map.getInternalName());

				mapData.setAttempts(dataFile.getInt(prePath + map.getInternalName() + ".attempts"));
				mapData.setFinishes(dataFile.getInt(prePath + map.getInternalName() + ".finishes"));
				mapData.setBestTime(dataFile.getLong(prePath + map.getInternalName() + ".besttime"));
				
				playerMapData.add(mapData);
			} else {
				// Create new section for this map
				dataFile.set(prePath + map.getInternalName() + ".attempts", 0);
				dataFile.set(prePath + map.getInternalName() + ".finishes", 0);
				dataFile.set(prePath + map.getInternalName() + ".besttime", (long) -1);
				dataFile.save();
				playerMapData.add(new PlayerMapData(map.getInternalName(), 0, 0, -1));
			}
		}
		
		pd.setMapData(playerMapData);		
		return pd;
	}
	
	/**
	 * Saves this data to the config. This will actually write to disk.
	 * This is used after changing a value
	 */
	public void save() {
		// Player's map data
		for (PlayerMapData pmd : mapData) {
			String prePath = uuid.toString() + ".maps.";
			dataFile.set(prePath + pmd.getInternalName() + ".attempts", pmd.getAttempts());
			dataFile.set(prePath + pmd.getInternalName() + ".finishes", pmd.getFinishes());
			dataFile.set(prePath + pmd.getInternalName() + ".besttime", pmd.getBestTime());
		}
		dataFile.save();
	}
	
	public PlayerMapData getMapData(String internalName) {
		for (PlayerMapData data : mapData) {
			if (data.getInternalName().equalsIgnoreCase(internalName)) {
				return data;
			}
		}
		
		return null;
	}
	
	public PlayerData(UUID playerId) {
		uuid = playerId;
	}

	public UUID getUuid() {
		return uuid;
	}

	public PlayerData setUuid(UUID uuid) {
		this.uuid = uuid;
		return this;
	}

	public ArrayList<PlayerMapData> getMapData() {
		return mapData;
	}

	public PlayerData setMapData(ArrayList<PlayerMapData> mapData) {
		this.mapData = mapData;
		return this;
	}
	
	public static void resetMapTimeByUUID(UUID id, String course){
		PlayerData pd = PlayerData.getDataFor(id);
		if(course.equalsIgnoreCase("all")){
			for (PlayerMapData data : pd.mapData) {
				data.setBestTime(-1L);
			}
			pd.save();
		}else{
			PlayerMapData mapData = pd.getMapData(course);
			if (mapData == null) {
				return;
			}
			mapData.setBestTime(-1L);
			pd.save();
		}
	}
	
}
