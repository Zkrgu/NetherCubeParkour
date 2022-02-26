package com.gmail.justbru00.nethercube.parkour.main;

import co.aikar.commands.PaperCommandManager;
import com.gmail.justbru00.nethercube.parkour.commands.*;
import com.gmail.justbru00.nethercube.parkour.gui.GUIManager;
import com.gmail.justbru00.nethercube.parkour.leaderboards.LeaderboardManager;
import com.gmail.justbru00.nethercube.parkour.listeners.IceTrackListener;
import com.gmail.justbru00.nethercube.parkour.listeners.MainGUIListener;
import com.gmail.justbru00.nethercube.parkour.listeners.PressurePlateTriggerListener;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;
import com.gmail.justbru00.nethercube.parkour.timer.PlayerTimer;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import com.gmail.justbru00.nethercube.parkour.utils.PluginFile;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.logging.Logger;

public class NetherCubeParkour extends JavaPlugin {
	
	public static ConsoleCommandSender console = Bukkit.getConsoleSender();
	public static Logger log = Bukkit.getLogger();
	public static String prefix = Messager.color("&8[&cNether&6Cube&fParkour&8] &6");
	private static NetherCubeParkour instance;
	public static PluginFile dataFile = null;
	public static boolean debug = true;
	public static boolean enableLeaderboards = true;

	@Override
	public void onDisable() {
		MapManager.saveMaps(getConfig());
		LeaderboardManager.saveLeaderboardPositions(getConfig());
		saveConfig();
		Messager.msgConsole("&cThe plugin is disabled.");
		instance = null;
	}

	@Override
	public void onEnable() {
		instance = this;
		
		Messager.msgConsole("&aEnabling plugin...");
		
		// INIT STUFF
		saveDefaultConfig();
		debug = getConfig().getBoolean("debug");		
		MapManager.init();
		dataFile = new PluginFile(this, "data.yml", "data.yml");		
		GUIManager.init();
		PlayerTimer.init();
		LeaderboardManager.loadLeaderboardLines();
		prefix = Messager.color(getConfig().getString("prefix"));
		
		
		// CHECK FOR HOLOGRAPHIC DISPLAYS
		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			Messager.msgConsole("&cWARNING HOLOGRAPHICDISPLAYS NOT INSTALLED OR ENABLED");
			Messager.msgConsole("&cDISABLING LEADERBOARDS.");
			enableLeaderboards = false;
		} else {
			LeaderboardManager.startUpdateTask();
		}
		
		// REGISTER LISTENERS
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new MainGUIListener(), instance);
		pm.registerEvents(new PressurePlateTriggerListener(), instance);
		pm.registerEvents(new IceTrackListener(), instance);

		// REGISTER COMMANDS
		PaperCommandManager manager = new PaperCommandManager(instance);
		manager.enableUnstableAPI("help");
		manager.registerCommand(new CourseCommand());
		manager.registerCommand(new LeaderboardCommand());
		manager.registerCommand(new BoatKillCommand());
		manager.registerCommand(new ParkourAdminCommand());
		manager.registerCommand(new LobbyCommand());

		// ADD AUTOCOMPLETE LISTS
		manager.getCommandCompletions().registerCompletion("loc", c -> {
			Player pl = c.getPlayer();
			Location target = pl.getTargetBlock((Set<Material>) null,6).getLocation();
			if(pl.getLocation().distance(target) > 5) target = pl.getLocation();
			return ImmutableList.of(target.getBlockX()+" "+target.getBlockY()+" "+target.getBlockZ());
		});
		manager.getCommandCompletions().registerCompletion("course", c -> {
			return ImmutableList.copyOf(MapManager.getMapNames());
		});
		manager.getCommandCompletions().registerCompletion("uuid", c -> {
			return ImmutableList.copyOf(dataFile.getKeys(false));
		});
	}
	
	public static NetherCubeParkour getInstance() {
		return instance;
	}

}
