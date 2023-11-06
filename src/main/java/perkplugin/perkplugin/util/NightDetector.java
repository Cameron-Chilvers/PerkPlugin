package perkplugin.perkplugin.util;


import org.bukkit.World;
import org.bukkit.entity.Player;

public class NightDetector {
    public static boolean isNight(World world) {
        long time = world.getTime();

        return time >= 13000 && time < 23000; // Night time is between 13000 (sunset) and 23000 (sunrise)
    }
    public static boolean isNight(Player player) {
        World world = player.getWorld();
        long time = world.getTime();

        return time >= 13000 && time < 23000; // Night time is between 13000 (sunset) and 23000 (sunrise)
    }
    public boolean isDay(Player player) {
        return !isNight(player);
    }

    public static boolean isDay(World world) {
        return !isNight(world);
    }
}