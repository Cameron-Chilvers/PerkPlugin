package perkplugin.perkplugin.util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import perkplugin.perkplugin.PerkPlugin;

public class InventoryUtil implements Listener {
    private PerkPlugin plugin;

    public InventoryUtil(PerkPlugin plugin){
        this.plugin = plugin;
    }

    public void saveInventory(Player player) {
        Inventory inventory = player.getInventory();
        ConfigUtil configUtil = new ConfigUtil(plugin, "player_inventories/" + player.getUniqueId() + ".yml");

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                configUtil.getConfig().set("inventory." + i, item.serialize());
            }
        }

        configUtil.save();
    }

    public void loadInventory(Player player, String invName) {
        ConfigUtil configUtil = new ConfigUtil(plugin, "player_inventories/" + player.getUniqueId() + ".yml");

        Inventory deathInv = Bukkit.createInventory(player, 9*5, invName);
        if (configUtil.getConfig().isConfigurationSection("inventory")) {
            for (String key : configUtil.getConfig().getConfigurationSection("inventory").getKeys(false)) {
                int slot = Integer.parseInt(key);
                ItemStack item = ItemStack.deserialize(configUtil.getConfig().getConfigurationSection("inventory." + key).getValues(true));
                deathInv.setItem(slot, item);
            }

            player.openInventory(deathInv);
        }else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dI Love you, But no inventory found <3"));
        }
    }

    public void deleteInventory(Player player){
        ConfigUtil configUtil = new ConfigUtil(plugin, "player_inventories/" + player.getUniqueId() + ".yml");
        configUtil.deleteFile();
    }

}
