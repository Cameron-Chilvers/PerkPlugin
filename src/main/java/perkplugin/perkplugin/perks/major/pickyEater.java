package perkplugin.perkplugin.perks.major;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.util.PermissionUtil;
import perkplugin.perkplugin.util.buffUtil;


public class pickyEater {
    private PerkPlugin plugin;
    private buffUtil buffUtil = new buffUtil();

    public pickyEater(PerkPlugin plugin){
        this.plugin = plugin;
    }

    public void pickyBuff(Player player, ItemStack itemStack, PlayerItemConsumeEvent event){
        if(new PermissionUtil(plugin).checkForPermission(player,"picky-eater")) {
            ItemStack pickyItem = new ItemStack(Material.valueOf(plugin.perkPointer.get(player.getUniqueId().toString()).get("picky-eater")));

            Bukkit.getLogger().info(pickyItem.getType().toString());

            if (itemStack.getType() != pickyItem.getType()) {
                event.setCancelled(true);
                return;
            }

            // Give buffs here
            buffUtil.giveBuff(player, PotionEffectType.REGENERATION, 75, 5);
            buffUtil.giveBuff(player, PotionEffectType.FAST_DIGGING, 75, 1);
            buffUtil.giveBuff(player, PotionEffectType.HEALTH_BOOST, 75, 2);
            buffUtil.giveBuff(player, PotionEffectType.SATURATION, 75, 1);
            buffUtil.giveBuff(player, PotionEffectType.INCREASE_DAMAGE, 75, 1);

            Bukkit.getLogger().info("item eaten " + itemStack.getType());
        }
    }


}
