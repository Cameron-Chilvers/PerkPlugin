package perkplugin.perkplugin.perks.major;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.perks.Perks;
import perkplugin.perkplugin.util.NightDetector;

public class Vampirism extends Perks {
    public final String perkName = "vampirism";
    private PerkPlugin plugin;
    public Vampirism(PerkPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void giveBuff(Player player){
        boolean isNight = NightDetector.isNight(Bukkit.getWorld("world"));


        plugin.perkMap.get(perkName).changePlayerType(player.getUniqueId().toString(), isNight, plugin);
    }

    @Override
    public String getName(){
        return this.perkName;
    }
}
