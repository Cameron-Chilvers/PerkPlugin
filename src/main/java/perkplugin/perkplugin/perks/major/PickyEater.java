package perkplugin.perkplugin.perks.major;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.perks.Perks;
import perkplugin.perkplugin.util.PermissionUtil;
import perkplugin.perkplugin.util.BuffUtil;


public class PickyEater extends Perks {

    public final String perkName = "picky-eater";
    private PerkPlugin plugin;

    public PickyEater(PerkPlugin plugin){
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
