package com.gmail.justbru00.nethercube.parkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.gmail.justbru00.nethercube.parkour.data.PlayerData;
import com.gmail.justbru00.nethercube.parkour.leaderboards.LeaderboardManager;
import com.gmail.justbru00.nethercube.parkour.map.Map;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import org.bukkit.Bukkit;
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
        @Syntax("<course> <pos>")
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
        @Subcommand("time")
        @Description("Set the best time of a player")
        @CommandCompletion("@course @uuid")
        @Syntax("<course> <uuid> <time>")
        public void setLeaderboardTime(CommandSender sender, String course, String uuid, long time){
            Map map = MapManager.getMap(course);
            if(map == null){
                Messager.msgSender("Course does not exist", sender);
                return;
            }
            UUID id;
            try{
                id = UUID.fromString(uuid);
            }catch (IllegalArgumentException e){
                Messager.msgSender("&cUhh... " + uuid+ " doesn't appear to be properly formatted UUID string. Fix that please. It really helps my sanity.", sender);
                return;
            }
            PlayerData pd = PlayerData.getDataFor(id);
            pd.getMapData(course).setBestTime(time);
            pd.save();
        }
    }
    @Subcommand("reset")
    @Description("Reset the best time for a player on a course")
    @CommandCompletion("@course|all @uuid|all")
    @Syntax("<course|all> <uuid|all>")
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
    @Subcommand("get")
    @Description("Get the top times for a course")
    @CommandCompletion("@course")
    @Syntax("<course> [limit]")
    public static void getTopTimes(CommandSender sender, String course, int limit){
        Map map = MapManager.getMap(course);
        if(map == null){
            Messager.msgSender("Course does not exist", sender);
            return;
        }

        java.util.Map<UUID, Long> bestTimes = LeaderboardManager.getFastestTimesForMap(course, limit);
        int i = 1;
        for(java.util.Map.Entry<UUID, Long> time : bestTimes.entrySet()){
            String playerName = Bukkit.getOfflinePlayer(time.getKey()).getName();
            Messager.msgSender(String.format("#%s - %s | %s", i, playerName, Messager.formatAsTime(time.getValue())),
                    sender);
            ++i;
        }
    }
    @HelpCommand
    public static void doHelp(CommandSender sender, CommandHelp help){
        help.showHelp();
    }
}
