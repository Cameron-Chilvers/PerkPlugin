package perkplugin.perkplugin.perks.major;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.util.buffUtil;

public class nakedWarrior {
    private PerkPlugin plugin;
    private buffUtil buffUtil = new buffUtil();

    public nakedWarrior(PerkPlugin plugin){
        this.plugin = plugin;
    }

    public void nakedBuff(Player player){
        buffUtil.giveBuff(player, PotionEffectType.DAMAGE_RESISTANCE, 99999, 5);
        buffUtil.giveBuff(player, PotionEffectType.FIRE_RESISTANCE, 99999, 5);
        buffUtil.giveBuff(player, PotionEffectType.HEALTH_BOOST, 99999, 2);
        buffUtil.giveBuff(player, PotionEffectType.LUCK, 99999, 1);
    }
}
