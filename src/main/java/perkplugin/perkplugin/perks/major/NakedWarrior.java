package perkplugin.perkplugin.perks.major;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.perks.Perks;
import perkplugin.perkplugin.util.BuffUtil;

public class NakedWarrior extends Perks {
    public final String perkName = "naked-warrior";
    private PerkPlugin plugin;

    public NakedWarrior(PerkPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void giveBuff(Player player){
        plugin.perkMap.get(perkName).changePlayerType(player.getUniqueId().toString(), true, plugin);
    }

    @Override
    public String getName(){
        return this.perkName;
    }
}
