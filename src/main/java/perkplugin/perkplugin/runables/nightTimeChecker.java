package perkplugin.perkplugin.runables;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.perks.major.vampirism;
import perkplugin.perkplugin.util.NightDetector;

public class nightTimeChecker {
    private PerkPlugin plugin;
    public nightTimeChecker(PerkPlugin plugin){
        this.plugin = plugin;
    }

    public void nightTimeChecker(){
        new BukkitRunnable() {
            @Override
            public void run() {
                vampirism vampirism = new vampirism();

                for (World world : Bukkit.getWorlds()) {
                    if(world.getName().equals("world")) {
                        if (NightDetector.isNight(world) && plugin.playerPermissions.containsKey("vampirism")) {
                            // Night has started in this world, do something
                            for (String playerUUID : plugin.playerPermissions.get("vampirism")) {
                                if (plugin.perkPointer.containsKey(playerUUID)) {
                                    // Vamp Buffs
                                    vampirism.vampBuff(playerUUID, true);
                                }
                            }
                        } else if (NightDetector.isDay(world) && plugin.playerPermissions.containsKey("vampirism")) {
                            for (String playerUUID : plugin.playerPermissions.get("vampirism")) {
                                // Vamp Debuffs
                                if (plugin.perkPointer.containsKey(playerUUID)) {
                                    vampirism.vampBuff(playerUUID, false);
                                }
                            }
                        }
                    }
                }
            }
            // this runs every min
        }.runTaskTimer(plugin, 0, 20*30); // Run every 30 seconds
    }

}
