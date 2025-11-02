package perkplugin.perkplugin.runables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.perks.major.FishyBoi;
import perkplugin.perkplugin.perks.major.Vampirism;
import perkplugin.perkplugin.perks.minor.DwarvenAptitude;

import static perkplugin.perkplugin.util.BuffUtil.getPlayerFromUUIDString;

public class SchedulerManager {

    private PerkPlugin plugin;
    private FishyBoi fishyBoi;
    private Vampirism vampirism;

    private DwarvenAptitude dwarvenAptitude;
    public SchedulerManager(PerkPlugin plugin){
        this.plugin = plugin;
        this.vampirism = new Vampirism(this.plugin);
        this.fishyBoi = new FishyBoi(this.plugin);
        this.dwarvenAptitude = new DwarvenAptitude(this.plugin);
    }

    public void EverySecondRunnable(){
        Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            fishyboiChecker();

            vampirismChecker();

            dwarvenAptitudeChecker();

        }, 0, 20 * 3); // Run every 1 second (20 ticks)
    }

    private void fishyboiChecker(){
        if(!plugin.perkMap.containsKey("fishy-boi")) {
            return;
        }

        // All players in the perk
        for (String playerStr : plugin.perkMap.get("fishy-boi").returnAllPlayers()) {
            Player player = getPlayerFromUUIDString(playerStr);

            // All players online
            if(plugin.playerMap.containsKey(playerStr)) {
                this.fishyBoi.giveBuff(player);
            }
        }
    }

    private void vampirismChecker(){
        if(!plugin.perkMap.containsKey("vampirism")){
            return;
        }

        // Night has started in this world, do something
        for (String playerUUID : plugin.perkMap.get("vampirism").returnAllPlayers()) {
            if (plugin.playerMap.containsKey(playerUUID)) {
                // Vamp Buffs
                vampirism.giveBuff(plugin.playerMap.get(playerUUID).getPlayer());
            }
        }
    }

    private  void dwarvenAptitudeChecker(){
        if(!plugin.perkMap.containsKey("dwarven-aptitude")){
            return;
        }

        // Checking the
        for (String playerUUID : plugin.perkMap.get("dwarven-aptitude").returnAllPlayers()) {
            if (plugin.playerMap.containsKey(playerUUID)) {
                // Vamp Buffs
                dwarvenAptitude.giveBuff(plugin.playerMap.get(playerUUID).getPlayer());
            }
        }
    }
}
