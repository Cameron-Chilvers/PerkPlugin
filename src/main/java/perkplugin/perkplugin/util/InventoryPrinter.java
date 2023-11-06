package perkplugin.perkplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryPrinter {

    public void printInventoryToConsole(Player player) {
        Inventory inventory = player.getInventory();

        Bukkit.getLogger().info("Printing inventory contents for player: " + player.getName());
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                Bukkit.getLogger().info("Slot " + i + ": " + item.getType() + " x " + item.getAmount());
            }
        }
        Bukkit.getLogger().info("Inventory printing complete.");
    }
}
