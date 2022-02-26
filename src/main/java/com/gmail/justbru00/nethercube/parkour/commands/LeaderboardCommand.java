package com.gmail.justbru00.nethercube.parkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.gmail.justbru00.nethercube.parkour.data.PlayerData;
import com.gmail.justbru00.nethercube.parkour.leaderboards.LeaderboardManager;
import com.gmail.justbru00.nethercube.parkour.map.Map;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.gmail.justbru00.nethercube.parkour.main.NetherCubeParkour.dataFile;

@CommandAlias("leaderboard")
@Description("Manage leaderboards")
@CommandPermission("nethercubeparkour.parkouradmin")
public class LeaderboardCommand extends BaseCommand {
    public LeaderboardCommand() {
    }

    @Subcommand("reload")
    @Description("Reload the hologram for the given course")
    @CommandCompletion("@course|all")
    public static void reloadLeaderboard(CommandSender sender, String course) {
        if (course.equals("all")) {
            LeaderboardManager.updateAllFastestTimeLeaderboard(sender);
        } else {
            if(!MapManager.getMapNames().contains(course)){
                Messager.msgSender("Course does not exist", sender);
            }
            LeaderboardManager.updateFastestTimeLeaderboard(course);
        }

    }
    @Subcommand("set")
    public class setLeaderboard extends BaseCommand {
        @Subcommand("pos")
        @CommandCompletion("@course @loc")
        public void setLeaderboardPos(Player sender, String course, float x, float y, float z){
            World world = sender.getWorld();
            Location loc = new Location(world,x,y,z);

            Map map = MapManager.getMap(course);
            if(map!=null){
                LeaderboardManager.setLeaderboardPosition(course, loc);
            }else{
                Messager.msgSender("Course does not exist", sender);
            }
        }
    }
    @Subcommand("reset")
    @CommandCompletion("@course|all @uuid|all")
    public static void resetLeaderboard(CommandSender sender, String course, String uuid){
        if(uuid.equalsIgnoreCase("all")) {
            for(String k : dataFile.getKeys(false)) {
                UUID id = UUID.fromString(k);
                PlayerData.resetMapTimeByUUID(id, course);
            }
            Messager.msgSender("Reset all times for " + (course.equalsIgnoreCase("all")?"all courses":"course "+course) + ".", sender);
        } else {
            UUID id;
            try {
                id = UUID.fromString(uuid);
            } catch (IllegalArgumentException e) {
                Messager.msgSender("&cUhh... " + uuid+ " doesn't appear to be properly formatted UUID string. Fix that please. It really helps my sanity.", sender);
                return;
            }
            PlayerData.resetMapTimeByUUID(id, course);
        }
    }
    @HelpCommand
    public static void doHelp(CommandSender sender, CommandHelp help){
        help.showHelp();
    }
}
