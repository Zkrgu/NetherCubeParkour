package com.gmail.justbru00.nethercube.parkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.gmail.justbru00.nethercube.parkour.map.Map;
import com.gmail.justbru00.nethercube.parkour.map.MapManager;
import com.gmail.justbru00.nethercube.parkour.utils.ItemBuilder;
import com.gmail.justbru00.nethercube.parkour.utils.Messager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("course")
@CommandPermission("nethercubeparkour.parkouradmin")
public class CourseCommand extends BaseCommand {

    @Subcommand("add")
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
    public static class SetCourseCommand extends BaseCommand{

        @Subcommand("spawn")
        @CommandCompletion("@course @loc")
        @Syntax("<map> <pos> [yaw] <pitch>")
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
                Messager.msgSender("Map does not exist", sender);
            }
        }

        @Subcommand("start")
        @CommandCompletion("@course @loc")
        @Syntax("<map> <pos>")
        public void setCourseStart(Player sender, String course, Double x, Double y, Double z) {
            World world = sender.getWorld();
            Location loc = new Location(world,x,y,z);

            Map map = MapManager.getMap(course);
            if(map!=null){
                map.setStartPlateLocation(loc);
            }else{
                Messager.msgSender("Map does not exist", sender);
            }
        }
        @Subcommand("end")
        @CommandCompletion("@course @loc")
        @Syntax("<map> <pos>")
        public void setCourseEnd(Player sender, String course, Double x, Double y, Double z) {
            World world = sender.getWorld();
            Location loc = new Location(world,x,y,z);

            Map map = MapManager.getMap(course);
            if(map!=null){
                map.setEndPlateLocation(loc);
            }else{
                Messager.msgSender("Map does not exist", sender);
            }
        }

        @Subcommand("creator")
        public void setCourseCreator(Player sender, String course, String creator) {
            Map map = MapManager.getMap(course);
            if(map!=null){
                map.setCreatorName(creator);
            }else{
                Messager.msgSender("Map does not exist", sender);
            }
        }
    }
}
