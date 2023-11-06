package perkplugin.perkplugin.perks.major;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.potion.PotionEffectType;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.util.PermissionUtil;
import perkplugin.perkplugin.util.buffUtil;


public class insatiable {
    private PerkPlugin plugin;
    private buffUtil buffUtil = new buffUtil();

    public insatiable(PerkPlugin plugin){
        this.plugin = plugin;
    }

    public void insatiableActive(Player player, FoodLevelChangeEvent event){
        if(new PermissionUtil(plugin).checkForPermission(player,"insatiable")) {
            if (12 < event.getFoodLevel() && !buffUtil.hasBuff(player, PotionEffectType.HEALTH_BOOST)) {
                // Hunger bar has decreased
                buffUtil.giveBuff(player, PotionEffectType.HEALTH_BOOST, 9999999, 5);
                buffUtil.giveBuff(player, PotionEffectType.HEAL, 1, 10);

            } else if (event.getFoodLevel() <= 12) {
                player.setHealth(0.0);
            }
        }
    }
}
