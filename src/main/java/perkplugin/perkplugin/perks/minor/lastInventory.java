package perkplugin.perkplugin.perks.minor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.util.InventoryUtil;
import perkplugin.perkplugin.util.PermissionUtil;

public class lastInventory implements Listener, CommandExecutor {
    private PerkPlugin plugin;
    private InventoryUtil inventoryUtil;
    private int items = 3;
    public lastInventory(PerkPlugin plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.inventoryUtil = new InventoryUtil(plugin);
    }
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event){
        if(!event.getView().getTitle().equals("StOnK")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!event.getView().getTitle().equals("StOnK")){
            return;
        }

        Player player = (Player) event.getWhoClicked();
        // Check Permissons
        int slot = event.getRawSlot();

        // Only want chest slot
        if(slot > 35){
            return;
        }

        if(items == 0) {
            event.setCancelled(true);
        }else {
            items--;
        }
        Bukkit.getLogger().info("NUM CLICKED " + Integer.toString(items) + " AND SLOT " + Integer.toString(slot));
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String Label, String [] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("Players ONLY");
            return true;
        }
        Player player = (Player) sender;

        Bukkit.getLogger().info(plugin.playerPermissions.toString());
        Bukkit.getLogger().info(String.valueOf(plugin.playerPermissions.containsKey("prized-possessions")));
        if(!new PermissionUtil(plugin).checkForPermission(player, "prized-possessions")) {
            sender.sendMessage(ChatColor.RED + "You do not have this perk");
            return true;
        }

        items = 3;

        inventoryUtil.loadInventory(player, "StOnK");
        inventoryUtil.deleteInventory(player);
        return true;
    }
}