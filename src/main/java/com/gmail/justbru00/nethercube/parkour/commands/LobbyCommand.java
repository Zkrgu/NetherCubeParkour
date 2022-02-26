package com.gmail.justbru00.nethercube.parkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.gmail.justbru00.nethercube.parkour.timer.PlayerTimer;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import org.bukkit.entity.Player;

@CommandAlias("lobby")
@Description("Teleport to the lobby")
@CommandPermission("nethercubeparkour.parkourlobby")
public class LobbyCommand extends BaseCommand {
	@Default
	public static void teleportToLobby(Player sender){
		PlayerTimer.playerLeavingMap(sender, true);
		Messager.msgSender("&6Teleported you to the parkour lobby.", sender);
	}
}
