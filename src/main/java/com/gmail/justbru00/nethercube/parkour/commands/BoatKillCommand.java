package com.gmail.justbru00.nethercube.parkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

@CommandAlias("boatkill")
@Description("Kills all boats without players")
@CommandPermission("nethercubeparkour.boatkill")
public class BoatKillCommand extends BaseCommand {
	@Default
	public static void killBoats(CommandSender sender) {
		long numerOfBoats = 0;

		for(World w : Bukkit.getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e.getType().equals(EntityType.BOAT)){
					if(e.isEmpty()){
						e.remove();
						++numerOfBoats;
					}
				}
			}
		}
		Messager.msgSender("&aRemoved " + numerOfBoats + " boats.", sender);
	}
}
