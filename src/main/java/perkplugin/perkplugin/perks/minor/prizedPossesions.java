package perkplugin.perkplugin.perks.minor;

import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.util.InventoryUtil;


public class prizedPossesions {
    private PerkPlugin plugin;

    private InventoryUtil inventoryUtil;
    public prizedPossesions(PerkPlugin plugin){
        this.plugin = plugin;
        this.inventoryUtil = new InventoryUtil(plugin);
    }

    /*public Boolean prizedPosActivate(CommandSender sender, Command command, String Label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("Players ONLY");
            return true;
        }
        Player player = (Player) sender;

        Bukkit.getLogger().info(plugin.playerPermissions.toString());
        Bukkit.getLogger().info(String.valueOf(plugin.playerPermissions.containsKey("prized-possessions")));
        if(!PermissionUtil.checkForPermission(player, "prized-possessions")) {
            sender.sendMessage(ChatColor.RED + "You do not have this perk");
            return true;
        }

        items = 3;

        inventoryUtil.loadInventory(player, "StOnK");
        inventoryUtil.deleteInventory(player);
        return true;
    }*/
}
