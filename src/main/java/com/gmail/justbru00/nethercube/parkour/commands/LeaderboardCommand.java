package com.gmail.justbru00.nethercube.parkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.gmail.justbru00.nethercube.parkour.leaderboards.LeaderboardManager;
import org.bukkit.command.CommandSender;

@CommandAlias("leaderboard")
@CommandPermission("nethercubeparkour.parkouradmin")
public class LeaderboardCommand extends BaseCommand {
    public LeaderboardCommand() {
    }

    @Subcommand("reload")
    @CommandCompletion("@course|all")
    public static void reloadLeaderboard(CommandSender sender, String course) {
        if (course.equals("all")) {
            LeaderboardManager.updateAllFastestTimeLeaderboard(sender);
        } else {
            LeaderboardManager.updateFastestTimeLeaderboard(course);
        }

    }
}
