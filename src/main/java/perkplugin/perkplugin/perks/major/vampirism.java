package perkplugin.perkplugin.perks.major;

import org.bukkit.potion.PotionEffectType;
import perkplugin.perkplugin.util.buffUtil;

public class vampirism {
    public vampirism(){

    }
    private buffUtil buffUtil = new buffUtil();
    public void vampBuff(String playerUUID, Boolean vampActivate){
        if (vampActivate){
            buffUtil.giveBuff(playerUUID, PotionEffectType.REGENERATION, 30, 1);
            buffUtil.giveBuff(playerUUID, PotionEffectType.FAST_DIGGING, 30, 1);
            buffUtil.giveBuff(playerUUID, PotionEffectType.HEALTH_BOOST, 30, 1);
            buffUtil.giveBuff(playerUUID, PotionEffectType.DAMAGE_RESISTANCE, 30, 1);
            buffUtil.giveBuff(playerUUID, PotionEffectType.INCREASE_DAMAGE, 30, 2);
            buffUtil.giveBuff(playerUUID, PotionEffectType.JUMP, 30, 1);
            buffUtil.giveBuff(playerUUID, PotionEffectType.LUCK, 30, 1);
            buffUtil.giveBuff(playerUUID, PotionEffectType.SPEED, 30, 2);
        }
        else{
            buffUtil.giveBuff(playerUUID, PotionEffectType.SLOW, 75, 1);
            buffUtil.giveBuff(playerUUID, PotionEffectType.WEAKNESS, 75, 1);
            buffUtil.giveBuff(playerUUID, PotionEffectType.UNLUCK, 75, 1);
            buffUtil.giveBuff(playerUUID, PotionEffectType.DARKNESS, 75, 1);
        }
    }
}
