package perkplugin.perkplugin.runables;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.util.buffUtil;

import static perkplugin.perkplugin.util.buffUtil.getPlayerFromUUIDString;

public class isInWaterChecker {
    private PerkPlugin plugin;
    private buffUtil buffUtil;

    public isInWaterChecker(PerkPlugin plugin){
        this.plugin = plugin;
        buffUtil = new buffUtil();
    }

    public void inWaterChecker(){
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if(plugin.playerPermissions.containsKey("fishy-boi")) {
                for (String playerStr : plugin.playerPermissions.get("fishy-boi")) {
                    Player player = getPlayerFromUUIDString(playerStr);
                    if(plugin.perkPointer.containsKey(playerStr)) {
                        applyBreathingEffect(player);
                    }
                }
            }
        }, 0, 20 * 3); // Run every 1 second (20 ticks)
    }

    private void applyBreathingEffect(Player player) {
        Location playerLocation = player.getLocation();
        Block blockUnderPlayer = playerLocation.getBlock().getRelative(0, -1, 0);
        Block blockAtPlayer = playerLocation.getBlock();

        if (player.isSwimming() || blockUnderPlayer.getType() == Material.WATER || blockAtPlayer.getType() == Material.WATER || blockUnderPlayer.getType() == Material.LAVA || blockAtPlayer.getType() == Material.LAVA) {
            if(blockUnderPlayer.getType() == Material.LAVA || blockAtPlayer.getType() == Material.LAVA){
                buffUtil.giveBuff(player, PotionEffectType.FIRE_RESISTANCE, 60, 5);
            }

            buffUtil.giveBuff(player, PotionEffectType.WATER_BREATHING, 30, 1);
            buffUtil.giveBuff(player, PotionEffectType.SPEED, 30, 2);
            buffUtil.giveBuff(player, PotionEffectType.DOLPHINS_GRACE, 30, 1);
            buffUtil.giveBuff(player, PotionEffectType.CONDUIT_POWER, 30, 5);
            buffUtil.giveBuff(player, PotionEffectType.INCREASE_DAMAGE, 30, 2);
            buffUtil.giveBuff(player, PotionEffectType.FAST_DIGGING, 30, 3);
        } else {
            buffUtil.giveBuff(player, PotionEffectType.HARM, 1, 1);
        }
    }
}
