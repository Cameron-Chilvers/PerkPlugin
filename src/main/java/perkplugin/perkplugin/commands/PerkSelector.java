package perkplugin.perkplugin.commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.perks.major.NakedWarrior;
import perkplugin.perkplugin.util.PermissionUtil;


import java.util.ArrayList;
import java.util.List;

public class PerkSelector implements Listener, CommandExecutor {
    private String perkSelectorTitle = "Perk Selector";
    private String foodSelectorTitle = "SELECT YOUR ONLY FOOD SOURCE";
    private PerkPlugin plugin;

    public PerkSelector(PerkPlugin plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    private void foodSelectorHelper(Player player){
        Inventory foodSelector = Bukkit.createInventory(player, 9*4, foodSelectorTitle);

        foodSelector.setItem(0, getItem(new ItemStack(Material.APPLE), "Apple"));
        foodSelector.setItem(1, getItem(new ItemStack(Material.GOLDEN_APPLE), "Golden Apple"));
        foodSelector.setItem(2, getItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), "Enchanted Golden Apple"));
        foodSelector.setItem(3, getItem(new ItemStack(Material.MELON_SLICE), "MELONB"));
        foodSelector.setItem(4, getItem(new ItemStack(Material.SWEET_BERRIES), "Berries"));
        foodSelector.setItem(5, getItem(new ItemStack(Material.GLOW_BERRIES), "Glow Berries"));
        foodSelector.setItem(6, getItem(new ItemStack(Material.CHORUS_FRUIT), "Chorus Fruit"));
        foodSelector.setItem(7, getItem(new ItemStack(Material.CARROT), "Carrot"));
        foodSelector.setItem(8, getItem(new ItemStack(Material.GOLDEN_CARROT), "Golden Carrot"));
        foodSelector.setItem(9, getItem(new ItemStack(Material.POTATO), "Potato"));
        foodSelector.setItem(10, getItem(new ItemStack(Material.BAKED_POTATO), "Baked Potoatto"));
        foodSelector.setItem(11, getItem(new ItemStack(Material.POISONOUS_POTATO), "POison potaot"));
        foodSelector.setItem(12, getItem(new ItemStack(Material.BEETROOT), "Beet root"));
        foodSelector.setItem(13, getItem(new ItemStack(Material.DRIED_KELP), "SEEWEED"));
        foodSelector.setItem(14, getItem(new ItemStack(Material.BEEF), "Blue Steak"));
        foodSelector.setItem(15, getItem(new ItemStack(Material.COOKED_BEEF), "Aussie Grass fed steak"));
        foodSelector.setItem(16, getItem(new ItemStack(Material.PORKCHOP), "Oink"));
        foodSelector.setItem(17, getItem(new ItemStack(Material.COOKED_PORKCHOP), "OInk2"));
        foodSelector.setItem(18, getItem(new ItemStack(Material.MUTTON), "SHep"));
        foodSelector.setItem(19, getItem(new ItemStack(Material.COOKED_MUTTON), "Lamb CHop"));
        foodSelector.setItem(20, getItem(new ItemStack(Material.CHICKEN), "Raw Chhhoook"));
        foodSelector.setItem(21, getItem(new ItemStack(Material.COOKED_CHICKEN), "Chargrill Charlie's"));
        foodSelector.setItem(22, getItem(new ItemStack(Material.RABBIT), "Bunny"));
        foodSelector.setItem(23, getItem(new ItemStack(Material.COOKED_RABBIT), "Cooked Rabbit"));
        foodSelector.setItem(24, getItem(new ItemStack(Material.COD), "Fish1"));
        foodSelector.setItem(25, getItem(new ItemStack(Material.COOKED_COD), "Cooked Fish1"));
        foodSelector.setItem(26, getItem(new ItemStack(Material.SALMON), "Fish2"));
        foodSelector.setItem(27, getItem(new ItemStack(Material.COOKED_SALMON), "Cooked Fish2"));
        foodSelector.setItem(28, getItem(new ItemStack(Material.TROPICAL_FISH), "Fish3"));
        foodSelector.setItem(29, getItem(new ItemStack(Material.PUFFERFISH), "Fish5"));
        foodSelector.setItem(30, getItem(new ItemStack(Material.BREAD), "BBBRRRRREEEEEEAAAAADDDDDDDDDDD"));
        foodSelector.setItem(31, getItem(new ItemStack(Material.COOKIE), "Cookie :)"));
        foodSelector.setItem(32, getItem(new ItemStack(Material.PUMPKIN_PIE), "Pumpkin + Pie"));
        foodSelector.setItem(33, getItem(new ItemStack(Material.MUSHROOM_STEW), "MUSHY STEW"));
        foodSelector.setItem(34, getItem(new ItemStack(Material.BEETROOT_SOUP), "SOUP"));
        foodSelector.setItem(35, getItem(new ItemStack(Material.RABBIT_STEW), "Not Vegan Soup"));

        player.openInventory(foodSelector);
    }

    private void perkSelectorHelper(InventoryClickEvent event){
        PermissionUtil permissionUtil = new PermissionUtil(plugin);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        switch(slot){
            case 0:
                player.sendMessage("Add prized-possessions");
                permissionUtil.addPermission(player, "prized-possessions");
                break;
            case 1:
                player.sendMessage("Remove prized-possessions");
                permissionUtil.removePermission(player, "prized-possessions");
                break;
            case 2:
                player.sendMessage("Add picky-eater");
                // Choose what food through here
                foodSelectorHelper(player);
                break;
            case 3:
                player.sendMessage("Remove picky-eater");
                permissionUtil.removePermission(player, "picky-eater");
                break;
            case 4:
                player.sendMessage("Add vampirism");
                permissionUtil.addPermission(player, "vampirism");
                break;
            case 5:
                player.sendMessage("Remove vampirism");
                permissionUtil.removePermission(player, "vampirism");
                break;
            case 6:
                player.sendMessage("Add Insatiable");
                permissionUtil.addPermission(player,"insatiable");
                break;
            case 7:
                player.sendMessage("Remove Insatiable");
                permissionUtil.removePermission(player, "insatiable");
                break;
            case 8:
                player.sendMessage("Add Fishy-Boi");
                permissionUtil.addPermission(player, "fishy-boi");
                break;
            case 9:
                player.sendMessage("Remove Fishy-Boi");
                permissionUtil.removePermission(player, "fishy-boi");
                break;
            case 10:
                player.sendMessage("Add Naked-Warrior");
                permissionUtil.addPermission(player, "naked-warrior");
                new NakedWarrior(plugin).giveBuff(player);
                break;
            case 11:
                player.sendMessage("Remove Naked-Warrior");
                permissionUtil.removePermission(player, "naked-warrior");
                break;
            case 12:
                player.sendMessage("Add Dwarven-Aptitude");
                permissionUtil.addPermission(player, "dwarven-aptitude");
                break;
            case 13:
                player.sendMessage("Remove Dwarven-Aptitude");
                permissionUtil.removePermission(player, "dwarven-aptitude");
                break;
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event){
        if (event.getView().getTitle().equals(perkSelectorTitle) || event.getView().getTitle().equals(foodSelectorTitle)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick_LOWEST(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Change this to be if has the naked perk
        if (new PermissionUtil(plugin).checkForPermission(player,"naked-warrior")) {
            if (event.getWhoClicked() instanceof Player) {
                int slot = event.getRawSlot();
                ItemStack clickedItem = event.getCurrentItem();

                // Check if the clicked item is armor
                if (clickedItem != null && isArmor(clickedItem.getType())) {
                    event.setCancelled(true); // Prevent equipping armor
                    player.sendMessage("You cannot use amour");
                }

            }
        }
    }

    @EventHandler()
    public void onInventoryClick_NORMAL(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(perkSelectorTitle)) {
            perkSelectorHelper(event);
            event.setCancelled(true);
            return;
        }

        if (event.getView().getTitle().equals(foodSelectorTitle)) {
            PermissionUtil permissionUtil = new PermissionUtil(plugin);
            Player player = (Player) event.getWhoClicked();

            Bukkit.getLogger().info(event.getCurrentItem().getType() + "");
            permissionUtil.addPermission(player, "picky-eater", String.valueOf(event.getCurrentItem().getType()));

            player.closeInventory();

            event.setCancelled(true);
            return;
        }

    }

    private boolean isArmor(Material material) {
        return material.toString().endsWith("_HELMET") ||
                material.toString().endsWith("_CHESTPLATE") ||
                material.toString().endsWith("_LEGGINGS") ||
                material.toString().endsWith("_BOOTS");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("Only Players");
        }

        Player player = (Player) sender;

        Inventory inv = Bukkit.createInventory(player, 9*3, perkSelectorTitle);

        inv.setItem(0, getItem(new ItemStack(Material.CHEST), "&9Prized Possesions", "&aDescription of Add Prized Possesions?", "&aWOOOOO"));
        inv.setItem(1, getItem(new ItemStack(Material.ENDER_CHEST), "&9Remove Prized Possesions", "&aRemove Prized Possestions?", "&aAAAAAAAAAAAAAAAAAA"));
        inv.setItem(2, getItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), "&9Picky Eater", "&aDescription of Add Picky Eater?", "&aWOOOOO"));
        inv.setItem(3, getItem(new ItemStack(Material.GOLDEN_APPLE), "&9Remove Picky Eater", "&Remove Picky Eater?", "&aBOOOOOOOOOOOO"));
        inv.setItem(4, getItem(new ItemStack(Material.BEETROOT), "&Add vampirism", "&aDescription Add vampirism?", "&aWOOOOO"));
        inv.setItem(5, getItem(new ItemStack(Material.BEETROOT_SEEDS), "&9Remove vampirism", "&Remove vampirism?", "&aBOOOOOOOOOOOO"));
        inv.setItem(6, getItem(new ItemStack(Material.COOKED_BEEF), "&Add Insatiable", "&Description Add  Insatiable?", "&aWOOOOO"));
        inv.setItem(7, getItem(new ItemStack(Material.BEEF), "&9Remove Insatiable", "&Remove Insatiable?", "&aBOOOOOOOOOOOO"));
        inv.setItem(8, getItem(new ItemStack(Material.COD), "&9Add Fishy Boi", "&9Description ofAdd Fishy Boi?", "&aBOOOOOOOOOOOO"));
        inv.setItem(9, getItem(new ItemStack(Material.COOKED_COD), "&9Remove Fishy Boi", "&9Remove Fishy Boi?", "&aBOOOOOOOOOOOO"));
        inv.setItem(10, getItem(new ItemStack(Material.DIAMOND_CHESTPLATE), "&9Add Naked Warrior", "&9Description of add Naked Warrior?", "&aBOOOOOOOOOOOO"));
        inv.setItem(11, getItem(new ItemStack(Material.GOLDEN_CHESTPLATE), "&9Remove Naked Warrior", "&9Remove Naked Warrior?", "&aBOOOOOOOOOOOO"));
        inv.setItem(12, getItem(new ItemStack(Material.DIAMOND_PICKAXE), "&Add Dwarven Aptitude", "&Add Dwarven Aptitude?", "&aBOOOOOOOOOOOO"));
        inv.setItem(13, getItem(new ItemStack(Material.GOLDEN_PICKAXE), "&9Remove Dwarven Aptitude", "&9Remove Dwarven Aptitude?", "&aBOOOOOOOOOOOO"));

        player.openInventory(inv);

        return true;
    }

    private ItemStack getItem(ItemStack item, String name, String ... lore){
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> lores = new ArrayList<>();
        for(String s : lore){
            lores.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }
}

