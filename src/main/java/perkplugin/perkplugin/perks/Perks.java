package perkplugin.perkplugin.perks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Perks {
    public void giveBuff(Player player) {
        Bukkit.getLogger().info("Give buff method not created");
    };

    public String getName(){
        Bukkit.getLogger().info("Perk Name not created");

        return "Failed :)";
    }
}
