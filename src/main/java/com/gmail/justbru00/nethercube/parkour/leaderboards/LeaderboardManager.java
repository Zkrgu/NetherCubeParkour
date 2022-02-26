package com.gmail.justbru00.nethercube.parkour.leaderboards;

import com.gmail.justbru00.nethercube.parkour.data.PlayerData;
import com.gmail.justbru00.nethercube.parkour.main.NetherCubeParkour;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.beta.hologram.Hologram;
import me.filoghost.holographicdisplays.api.beta.hologram.HologramLines;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class LeaderboardManager {
	private static List<String> fastestTimeBoardLines = new ArrayList<String>();
	private static HashMap<String,Location> fastestTimeBoardLocations = new HashMap<String,Location>();
	private static HashMap<String, Hologram> fastestHolograms = new HashMap<String, Hologram>();
	private static HolographicDisplaysAPI hologramsDisplayAPI = HolographicDisplaysAPI.get(NetherCubeParkour.getInstance());

	public static Map<UUID, Long> getFastestTimesForMap(String mapInternalName, int limit) {
		ArrayList<PlayerData> allTheData = new ArrayList<PlayerData>();

		for (String key : NetherCubeParkour.dataFile.getKeys(false)) {
			try {
				allTheData.add(PlayerData.getDataFor(UUID.fromString(key)));
			} catch (Exception e) {
				Messager.debug("&cFailed to get data for uuid: " + key);
			}
		}

		HashMap<UUID, Long> dataMap = new HashMap<UUID, Long>();

		for (PlayerData v : allTheData) {
			if (v.getMapData(mapInternalName).getBestTime() != -1) {
				dataMap.put(v.getUuid(), v.getMapData(mapInternalName).getBestTime());
			}
		}
		// Get the top times
		Map<UUID, Long> top = dataMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.naturalOrder())).limit(limit)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		return top;
	}

	public static void updateFastestTimeLeaderboard(String mapInternalName) {
		if (!NetherCubeParkour.enableLeaderboards) {
			return;
		}
		Location loc = fastestTimeBoardLocations.get(mapInternalName);

		if(loc == null) return;

		com.gmail.justbru00.nethercube.parkour.map.Map map = MapManager.getMap(mapInternalName);

		ArrayList<PlayerData> allTheData = new ArrayList<PlayerData>();

		for (String key : NetherCubeParkour.dataFile.getKeys(false)) {
			try {
				allTheData.add(PlayerData.getDataFor(UUID.fromString(key)));
			} catch (Exception e) {
				Messager.debug("&cFailed to get data for uuid: " + key);
			}
		}

		HashMap<UUID, Long> dataMap = new HashMap<UUID, Long>();

		for (PlayerData v : allTheData) {
			if (v.getMapData(mapInternalName).getBestTime() != -1) {
				dataMap.put(v.getUuid(), v.getMapData(mapInternalName).getBestTime());
			}
		}
		// Get the top ten
		Map<UUID, Long> topTen = dataMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.naturalOrder())).limit(10)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		List<String> textLines = new ArrayList<String>();

		ArrayList<UUID> orderedIds = new ArrayList<UUID>();

		for (Entry<UUID, Long> entry : topTen.entrySet()) {
			orderedIds.add(entry.getKey());
		}

		for (String line : fastestTimeBoardLines) {
			// Replace the map name
			line = line.replace("{mapname}",
					MapManager.getMap(mapInternalName).getGuiItem().getItemMeta().getDisplayName());

			// Replace names
			for (int i = 1; i <= 10; i++) {
				String name;
				try {
					name = Bukkit.getOfflinePlayer(orderedIds.get(i - 1)).getName();
				} catch (IndexOutOfBoundsException e) {
					name = "Empty";
				}

				line = line.replace("{name" + i + "}", name);
			}

			// Replace Times
			for (int i = 1; i <= 10; i++) {
				String time;
				try {
					time = Messager.formatAsTime(topTen.get(orderedIds.get(i - 1)));
				} catch (IndexOutOfBoundsException e) {
					time = "none";
				}

				line = line.replace("{time" + i + "}", time);
			}

			textLines.add(line);
		}
		Bukkit.getScheduler().runTask(NetherCubeParkour.getInstance(), new Runnable() {

			@Override
			public void run() {
				// Update actual hologram
				// Hologram naming method: fastest_mapinternalname - If I could name them lol
				Hologram holo = fastestHolograms.computeIfAbsent(mapInternalName, k -> hologramsDisplayAPI.createHologram(loc));
				if(!holo.getPosition().toLocation().equals(loc)) holo.setPosition(loc);
				HologramLines hololines = holo.getLines();
				hololines.clear();

				for (String line : textLines) {
					hololines.appendText(Messager.color(line));
				}
				Messager.debug("[LeaderManager] Finished updating fastest time leaderboard for " + map.getInternalName()
						+ ".");
			}
		});

	}

	/**
	 * Updates all fastest time leaderboards by calling
	 * {@link #updateFastestTimeLeaderboard(String)} for every map in the MapManager
	 */
	public static void updateAllFastestTimeLeaderboard() {
		if (!NetherCubeParkour.enableLeaderboards) {
			return;
		}
		for (com.gmail.justbru00.nethercube.parkour.map.Map m : MapManager.getMaps()) {
			updateFastestTimeLeaderboard(m.getInternalName());
		}
	}

	/**
	 * Updates all fastest time leaderboards by calling
	 * {@link #updateFastestTimeLeaderboard(String)} for every map in the MapManager
	 * 
	 * @param toNotify The {@link CommandSender} that should be notified of this
	 *                 finishing
	 */
	public static void updateAllFastestTimeLeaderboard(CommandSender toNotify) {
		if (!NetherCubeParkour.enableLeaderboards) {
			return;
		}
		for (com.gmail.justbru00.nethercube.parkour.map.Map m : MapManager.getMaps()) {
			updateFastestTimeLeaderboard(m.getInternalName());
			Messager.msgSender("&aUpdated the fastest time leaderboard for: " + m.getInternalName(), toNotify);
		}
		Messager.msgSender("&aFinished updating all of the fastest time leaderboards.", toNotify);
	}

	/**
	 * This will not be reloaded with /elyadmin reload
	 */
	public static void startUpdateTask() {
		int ticksBetweenUpdates = NetherCubeParkour.getInstance().getConfig()
				.getInt("leaderboards.update_every_x_ticks");
		Bukkit.getScheduler().runTaskTimerAsynchronously(NetherCubeParkour.getInstance(), new Runnable() {
			@Override
			public void run() {
				Messager.debug("Starting auto leaderboard update.");
				updateAllFastestTimeLeaderboard();

				Messager.debug("Finished auto leaderboard update.");
			}
		}, 30 * 20, ticksBetweenUpdates);
	}

	public static void loadLeaderboardLines() {
		FileConfiguration config = NetherCubeParkour.getInstance().getConfig();

		// Clear to allow for reloading
		fastestTimeBoardLocations.clear();

		// Load Fastest Time leaderboard lines
		fastestTimeBoardLines = config.getStringList("leaderboards.fastesttime.lines");

		for (String key : config.getConfigurationSection("leaderboards.fastesttime.locations").getKeys(false)) {
			String prefix = "leaderboards.fastesttime.locations." + key + ".";
			fastestTimeBoardLocations.put(key, new Location(Bukkit.getWorld(config.getString(prefix + "world")),
					config.getDouble(prefix + "x"), config.getDouble(prefix + "y"), config.getDouble(prefix + "z")));
		}

	}

	public static void setLeaderboardPosition(String course, Location loc) {
		if(fastestTimeBoardLocations.containsKey(course)){
			fastestTimeBoardLocations.replace(course, loc);
		}else {
			fastestTimeBoardLocations.put(course, loc);
		}
		Messager.msgConsole(fastestTimeBoardLocations.get(course).toString());
	}
	public static void saveLeaderboardPositions(FileConfiguration c){
		for(Entry<String, Location> courseLoc : fastestTimeBoardLocations.entrySet()){
			String course = courseLoc.getKey();
			Location loc = courseLoc.getValue();

			saveLeaderboardPosition(c, "leaderboards.fastesttime.locations", course, loc);
		}
		Messager.msgConsole("Saved all leaderboard positions.");
	}
	public static void saveLeaderboardPosition(FileConfiguration c, String root, String course, Location loc){
		String basePath = root + "." + course + ".";
		c.set(basePath + "x", loc.getX());
		c.set(basePath + "y", loc.getY());
		c.set(basePath + "z", loc.getZ());
		c.set(basePath + "world", loc.getWorld().getName());
	}
}
