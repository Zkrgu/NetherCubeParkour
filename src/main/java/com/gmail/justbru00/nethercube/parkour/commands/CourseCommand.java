package com.gmail.justbru00.nethercube.parkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.gmail.justbru00.nethercube.parkour.gui.GUIManager;
import com.gmail.justbru00.nethercube.parkour.map.Map;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;
import com.gmail.justbru00.nethercube.parkour.timer.PlayerTimer;
import com.gmail.justbru00.nethercube.parkour.utils.ItemBuilder;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

@CommandAlias("course|c")
@Description("Manage courses through commands")
@CommandPermission("nethercubeparkour.parkouradmin")
public class CourseCommand extends BaseCommand {

    @Subcommand("add")
    @Description("Add a course")
    public static void addCourse(CommandSender sender, String internalName, @Optional String[] displayName) {
        Map map = new Map(internalName);
        String name = internalName;
        if(displayName != null){
            name = String.join(" ",displayName);
        }
        map.setGuiItem(new ItemBuilder(Material.STONE).setName(name).build());
        map.setDisplayName(name);
        MapManager.addMap(map);
        Messager.msgSender("Successfully added "+internalName, sender);
    }
    @Subcommand("set")
    @Description("Modify an existing course")
    public class SetCourseCommand extends BaseCommand{

        @Subcommand("spawn")
        @Description("Set course spawn location")
        @CommandCompletion("@course @loc")
        @Syntax("<course> <pos> [yaw] <pitch>")
        public void setCourseSpawn(Player sender, String course, Double x, Double y, Double z, @Optional Float yaw, Float pitch) {
            World world = sender.getWorld();
            Location loc;
            if(pitch != null && yaw != null){
                loc = new Location(world,x,y,z,yaw,pitch);
            }
            else {
                loc = new Location(world,x,y,z);
            }
            Map map = MapManager.getMap(course);
            if(map!=null){
                map.setSpawnLocation(loc);
            }else{
                Messager.msgSender("Course does not exist", sender);
            }
        }

        @Subcommand("start")
        @Description("Set course start location")
        @CommandCompletion("@course @loc")
        @Syntax("<course> <pos>")
        public void setCourseStart(Player sender, String course, Double x, Double y, Double z) {
            World world = sender.getWorld();
            Location loc = new Location(world,x,y,z);

            Map map = MapManager.getMap(course);
            if(map!=null){
                map.setStartPlateLocation(loc);
            }else{
                Messager.msgSender("Course does not exist", sender);
            }
        }
        @Subcommand("end")
        @Description("Set course end location")
        @CommandCompletion("@course @loc")
        @Syntax("<course> <pos>")
        public void setCourseEnd(Player sender, String course, Double x, Double y, Double z) {
            World world = sender.getWorld();
            Location loc = new Location(world,x,y,z);

            Map map = MapManager.getMap(course);
            if(map!=null){
                map.setEndPlateLocation(loc);
            }else{
                Messager.msgSender("Course does not exist", sender);
            }
        }

        @Subcommand("creator")
        @Description("Set course creator")
        @CommandCompletion("@course")
        @Syntax("<course> <creator>")
        public void setCourseCreator(Player sender, String course, String creator) {
            Map map = MapManager.getMap(course);
            if(map!=null){
                map.setCreatorName(creator);
            }else{
                Messager.msgSender("Course does not exist", sender);
            }
        }
    }
    @Subcommand("start")
    @Description("Start a course for a given player")
    @CommandPermission("parkour.parkourstart")
    @CommandCompletion("@course @Players")
    @Syntax("<course> [player]")
    public static void startCourse(CommandSender sender, String course, @Optional Player p){
        if(p == null) {
            if(sender instanceof Player) {
                p = (Player) sender;
            }
            else{
                Messager.msgSender("&cCannot start course, no player selected.", sender);
                return;
            }
        }
        PlayerTimer.playerStartingMap(p, MapManager.getMap(course));
        Messager.msgSender("&aAttempted to start the player " + p.getDisplayName() + " on the course " + course, sender);
    }
    @Subcommand("checkpoint")
    @Description("Activate the checkpoint for a given player")
    @CommandCompletion("@course @Players")
    @Syntax("<course> [player]")
    public static void activateCheckpoint(CommandSender sender, String course, @Optional Player p){
        if(p == null){
            if(sender instanceof Player){
                p = (Player) sender;
            }
            else{
                Messager.msgSender("&cCannot start course, no player selected.", sender);
                return;
            }
        }
        PlayerTimer.playerCheckpointMap(p, MapManager.getMap(course));
        Messager.msgSender("&aAttempted to activate checkpoint for the player " + p.getDisplayName() + " on the course " + course, sender);
    }
    @Subcommand("stop")
    @Description("Finish a course for a given player")
    @CommandPermission("parkour.parkourstop")
    @CommandCompletion("@course @Players")
    @Syntax("<course> [player]")
    public static void endCourse(CommandSender sender, String course, @Optional Player p){
        if(p == null){
            if(sender instanceof Player){
                p = (Player) sender;
            }
            else{
                Messager.msgSender("&cCannot start course, no player selected.", sender);
                return;
            }
        }
        PlayerTimer.playerEndedMap(p, MapManager.getMap(course));
        Messager.msgSender("&aAttempted to stop the player " + p.getDisplayName() + " on the course " + course, sender);
    }
    @Subcommand("gui")
    @Description("Open course gui")
    @CommandPermission("nethercubeparkour.courses")
    public static void courseGUI(Player sender){
        GUIManager.openMainGUI(sender);
    }
    @Subcommand("tp")
    @Description("Teleport to a given course")
    @CommandPermission("nethercubeparkour.parkourtp")
    @CommandCompletion("@course @players")
    @Syntax("<course> [player]")
    public static void courseTp(CommandSender sender, String course, @Optional Player target) {
        Map map = MapManager.getMap(course);
        if(map == null){
            Messager.msgSender("Course does not exist", sender);
            return;
        }
        if(target == null) {
            if(!(sender instanceof Player)) {
                Messager.msgSender("No player provided", sender);
            }
            target = (Player) sender;
        }
        target.teleport(map.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        Messager.msgSender("&6Teleported " + target.getName() + " to the start of map " + map.getInternalName() + ".", sender);
    }
    @Subcommand("list")
    @Description("List all courses")
    public static void listCourses(CommandSender sender){
        for(Map m : MapManager.getMaps()){
            Messager.msgSender("&6" + m.getInternalName(), sender);
        }
    }
    @HelpCommand
    public static void doHelp(CommandSender sender, CommandHelp help){
        help.showHelp();
    }
}
