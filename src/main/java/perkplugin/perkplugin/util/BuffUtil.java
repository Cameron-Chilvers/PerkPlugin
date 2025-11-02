package perkplugin.perkplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BuffUtil {
    public void giveBuff(Player player, PotionEffectType type, int duration, int amplifier){
        PotionEffect potionEffect = new PotionEffect(type, duration * 20, amplifier - 1, true, false);

        // check if already have a perk
        player.addPotionEffect(potionEffect);
    }

    public void giveBuff(String playerString, PotionEffectType type, int duration, int amplifier){
        PotionEffect potionEffect = new PotionEffect(type, duration * 20, amplifier - 1, true, false);

        Player player = getPlayerFromUUIDString(playerString);
        player.addPotionEffect(potionEffect);
    }

    public static Player getPlayerFromUUIDString(String uuidString) {
        try {
            // Convert the UUID string to a UUID object
            java.util.UUID uuid = java.util.UUID.fromString(uuidString);

            // Get the Player object using the UUID
            return Bukkit.getPlayer(uuid);
        } catch (IllegalArgumentException e) {
            // Handle invalid UUID string
            return null;
        }
    }
    public boolean hasBuff(Player player, PotionEffectType type) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

}