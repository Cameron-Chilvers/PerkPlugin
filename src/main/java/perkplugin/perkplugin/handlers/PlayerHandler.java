package perkplugin.perkplugin.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.classes.PlayerCustom;
import perkplugin.perkplugin.perks.Perks;
import perkplugin.perkplugin.perks.major.*;
import perkplugin.perkplugin.util.*;

import java.util.ArrayList;
import java.util.List;


public class PlayerHandler implements Listener {
    private PerkPlugin plugin;
    private InventoryUtil inventoryUtil;
    private final List<Perks> startUpList = new ArrayList<>();


    public PlayerHandler(PerkPlugin plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        inventoryUtil = new InventoryUtil(plugin);

        startUpList.add(new Insatiable(plugin));
        startUpList.add(new NakedWarrior(plugin));
        startUpList.add(new FishyBoi(plugin));
        startUpList.add(new Vampirism(plugin));

    }

    private void setUpPlayerAllPerk(PlayerCustom player){
        String playerUUIDString = player.getPlayerUUIDString();
        // Check if file
        ConfigUtil configUtil = new ConfigUtil(plugin, "player_perks/" + playerUUIDString + ".yml");
        if(configUtil.getConfig().isConfigurationSection("Perks")){
            // Get perks and stuff and add to perk pointer
            Bukkit.getLogger().info("HAS PLAYER FILE");

            for (String key : configUtil.getConfig().getConfigurationSection("Perks").getKeys(false)) {
                String perkInfo = configUtil.getConfig().getConfigurationSection("Perks").getString(key);

                player.addPerk(key, perkInfo);
            }

        }else{
            // Add name to perkPointer but have it be empty
            Bukkit.getLogger().info("NO PLAYER FILE");
        }
    }

    private void setUpPermanentPerks(Player player){
        PermissionUtil permUtil = new PermissionUtil(plugin);

        for(Perks perk : startUpList){
            String perkName = perk.getName();
            if(!permUtil.checkForPermission(player, perkName)){
                continue;
            }

            perk.giveBuff(player);
        }
    }


    Player prevPLayer;

    public void printAllAttributes(Player player) {
        for (Attribute attribute : Attribute.values()) {
            AttributeInstance attributeInstance = player.getAttribute(attribute);

            if (attributeInstance != null) {
                double value = attributeInstance.getValue();
                Bukkit.getLogger().info(attribute.name() + ": " + value);
            }
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        PlayerCustom newPlayerCustom = new PlayerCustom(player, plugin);
        plugin.playerMap.put(player.getUniqueId().toString(), newPlayerCustom);

        if(prevPLayer != null){
            player.addPassenger(prevPLayer);
        }

        prevPLayer = player;

        if(player.getName().equals("cyberbears")){
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);

        }

        printAllAttributes(player);

        setUpPlayerAllPerk(newPlayerCustom);

        setUpPermanentPerks(player);

        /* TUTORIAL CODE TO GIVE TORCHES WHEN SPAWN IN
        ItemStack item = new ItemStack(Material.TORCH, 64 );
        Inventory inv = player.getInventory();

        ItemMeta meta = item.getItemMeta();
        meta.setLore(new ArrayList<>(Arrays.asList("Stinky", "stinky")));
        meta.setDisplayName("TEST");
        item.setItemMeta(meta);

        inv.addItem(item);
        inv.setItem(8, item);

        /*PermissionUtil permUtil = new PermissionUtil(plugin);
        permUtil.addPermission(player, "test");*/
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();

        PlayerCustom playerLeftObject = plugin.playerMap.get(player.getUniqueId().toString());
        playerLeftObject.saveToFile();

        plugin.playerMap.remove(player.getUniqueId().toString());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL)){
            return;
        }

        Player player = (Player) event.getEntity();


        /* TUTORIAL STUFF SHOWING DELAYED TASK
        DelayedTask task =  new DelayedTask(() -> {
            player.getInventory().addItem(new ItemStack(Material.DIAMOND));
        }, 20*5);

        Bukkit.getScheduler().cancelTask(task.getId());

        /*PermissionUtil permUtil = new PermissionUtil(plugin);
        permUtil.removePermission(player, "test");*/
    }

    @EventHandler // Maybe just change event to PlayerDeathEvent
    public void onPlayerDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        // Save last inventory to file then make command to read it
        if(new PermissionUtil(plugin).checkForPermission(player,"prized-possessions")){
            Bukkit.getLogger().info("INSIDE THE IF STATEMENT");
            inventoryUtil.saveInventory(player);

            new InventoryPrinter().printInventoryToConsole(player);
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent event){
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if(new PermissionUtil(plugin).checkForPermission(player,"naked-warrior")){
            ItemStack item = event.getItem().getItemStack();

            // Check if the item being picked up is armor
            if (isArmor(item.getType())) {
                event.setCancelled(true); // Prevent picking up armor
                player.sendMessage("You cannot pick up armor.");
            }
        }

    }

    // Helper method to check if an item type is armor
    private boolean isArmor(Material material) {
        return material.toString().endsWith("_HELMET") ||
                material.toString().endsWith("_CHESTPLATE") ||
                material.toString().endsWith("_LEGGINGS") ||
                material.toString().endsWith("_BOOTS");
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event){
        Player player = (Player) event.getPlayer();
        ItemStack itemStack = (ItemStack) event.getItem();

        // Getting picky Eater events
        // This gets spammed when the event goes off idk why
        String pickyEaterName = "picky-eater";
        if(new PermissionUtil(plugin).checkForPermission(player, pickyEaterName)) {
            ItemStack pickyItem = new ItemStack(Material.valueOf(plugin.playerMap.get(player.getUniqueId().toString()).getPerkItem(pickyEaterName)));

            Bukkit.getLogger().info(pickyItem.getType().toString());

            if (itemStack.getType() != pickyItem.getType()) {
                event.setCancelled(true);
                return;
            }

            new PickyEater(plugin).giveBuff(player);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // calling insatiable class
            new Insatiable(plugin).giveBuff(player);
        }
    }

    /*
    @EventHandler
    public void breakBlock(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(!player.hasPermission("tutorial.test")){
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "CANNOT Do");
        }
    }*/

}