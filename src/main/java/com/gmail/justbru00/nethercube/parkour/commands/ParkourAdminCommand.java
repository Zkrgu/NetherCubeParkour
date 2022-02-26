package com.gmail.justbru00.nethercube.parkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.gmail.justbru00.nethercube.parkour.gui.GUIManager;
import com.gmail.justbru00.nethercube.parkour.leaderboards.LeaderboardManager;
import com.gmail.justbru00.nethercube.parkour.main.NetherCubeParkour;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;
import com.gmail.justbru00.nethercube.parkour.timer.PlayerTimer;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import org.bukkit.command.CommandSender;

import static com.gmail.justbru00.nethercube.parkour.main.NetherCubeParkour.dataFile;


@CommandAlias("parkouradmin|padm")
@Description("Plugin utility commands")
@CommandPermission("nethercubeparkour.parkouradmin")
public class ParkourAdminCommand extends BaseCommand {
	@Subcommand("reload")
	public static void reload(CommandSender sender){
		NetherCubeParkour.getInstance().onDisable();
		NetherCubeParkour.getInstance().reloadConfig();
		dataFile.reload();
		Messager.msgSender("&aReloaded config.yml and data.yml.", sender);
		MapManager.init();
		GUIManager.init();
		PlayerTimer.init();
		LeaderboardManager.loadLeaderboardLines();
		Messager.msgSender("&aReinitialized LeaderboardManager, MapManager, GUIManager, and PlayerManager.", sender);
	}
	@HelpCommand
	public static void doHelp(CommandSender sender, CommandHelp help){
		help.showHelp();
	}
}
