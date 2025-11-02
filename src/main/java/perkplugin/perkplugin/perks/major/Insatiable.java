package perkplugin.perkplugin.perks.major;

import org.bukkit.entity.Player;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.perks.Perks;
import perkplugin.perkplugin.util.PermissionUtil;


public class Insatiable extends Perks {
    public final String perkName = "insatiable";
    private PerkPlugin plugin;
    private final int instatiableIndicator = 11;
    public Insatiable(PerkPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void giveBuff(Player player){
        if(new PermissionUtil(plugin).checkForPermission(player,perkName)) {
            if (instatiableIndicator < player.getFoodLevel()) {
                // Setting true for insatiable perk
                plugin.perkMap.get(perkName).changePlayerType(player.getUniqueId().toString(), true, plugin);

            } else if (player.getFoodLevel() <= instatiableIndicator) {
                // Removing buff
                plugin.perkMap.get(perkName).changePlayerType(player.getUniqueId().toString(), false, plugin);

                // Killing player
                player.setHealth(0.0);
            }
        }
    }

    @Override
    public String getName(){
        return this.perkName;
    }
}
