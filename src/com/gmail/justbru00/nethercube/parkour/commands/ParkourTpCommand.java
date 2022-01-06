package com.gmail.justbru00.nethercube.parkour.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.justbru00.nethercube.parkour.data.PlayerData;
import com.gmail.justbru00.nethercube.parkour.map.Map;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;

public class ParkourTpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// /elytatp <map> <player>
		if (command.getName().equalsIgnoreCase("parkourtp")) {
			if (sender.hasPermission("nethercubeparkour.parkourtp")) {
				if (args.length == 2) {
					Map map = MapManager.getMap(args[0]);
					String playerName = args[1];

					if (map != null) {
						Player target;
						try {
							target = Bukkit.getPlayer(playerName);
						} catch (Exception e) {
							Messager.msgSender("&cProvided player is not online.", sender);
							return true;
						}
						if (target == null) {
							Messager.msgSender("&cProvided player is not online.", sender);
							return true;
						}

						PlayerData pd = PlayerData.getDataFor(target);
						// TELEPORT PLAYER
						target.teleport(map.getSpawnLocation(), TeleportCause.PLUGIN);
						Messager.msgSender("&6Teleported " + target.getName() + " to the start of map " + map.getInternalName() + ".", sender);
						return true;
					} else {
						Messager.msgSender("&cThe map '" + args[0] + "' could not be found. Is it spelled correctly?",
								sender);
						return true;
					}
				} else {
					Messager.msgSender("&cPlease provide correct arguments. /parkourtp <map> <player>", sender);
					return true;
				}
			} else {
				Messager.msgSender("&cYou don't have the permission to use that command.", sender);
				return true;
			}
		}

		return false;
	}

}
