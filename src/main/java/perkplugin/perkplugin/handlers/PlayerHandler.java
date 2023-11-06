package perkplugin.perkplugin.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.perks.major.insatiable;
import perkplugin.perkplugin.perks.major.nakedWarrior;
import perkplugin.perkplugin.perks.major.pickyEater;
import perkplugin.perkplugin.util.*;

import java.util.HashMap;

public class PlayerHandler implements Listener {
    private PerkPlugin plugin;
    private InventoryUtil inventoryUtil;

    public PlayerHandler(PerkPlugin plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        inventoryUtil = new InventoryUtil(plugin);
    }

    public void setUpPerkPointer(Player player){
        // Check if file
        ConfigUtil configUtil = new ConfigUtil(plugin, "player_perks/" + player.getUniqueId() + ".yml");
        if(configUtil.getConfig().isConfigurationSection("Perks")){
            // Get perks and stuff and add to perk pointer
            Bukkit.getLogger().info("HAS PLAYER FILE");
            plugin.perkPointer.put(player.getUniqueId().toString(), new HashMap<String, String>());

            for (String key : configUtil.getConfig().getConfigurationSection("Perks").getKeys(false)) {
                plugin.perkPointer.get(player.getUniqueId().toString()).put(key, configUtil.getConfig().getConfigurationSection("Perks").getString(key));
            }

            Bukkit.getLogger().info(plugin.perkPointer.toString());

        }else{
            // Add name to perkPointer but have it be empty
            Bukkit.getLogger().info("NO PLAYER FILE");
            plugin.perkPointer.put(player.getUniqueId().toString(), new HashMap<String, String>());
            Bukkit.getLogger().info(plugin.perkPointer.toString());

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

        if(prevPLayer != null){
            player.addPassenger(prevPLayer);
        }

        prevPLayer = player;

        if(player.getName().equals("cyberbears")){
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);

            AttributeInstance attackSpeedAttribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);

            /*
            for(AttributeModifier removeall : attackSpeedAttribute.getModifiers()){
                attackSpeedAttribute.removeModifier(removeall);
            }

            Bukkit.getLogger().info(attackSpeedAttribute.getModifiers().toString());*/

        }

        printAllAttributes(player);

        setUpPerkPointer(player);

        if(new PermissionUtil(plugin).checkForPermission(player,"naked-warrior")){
            new nakedWarrior(plugin).nakedBuff(player);
        }

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
        plugin.perkPointer.remove(player.getUniqueId().toString());
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
        new pickyEater(plugin).pickyBuff(player, itemStack, event);

    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // calling insatiable class
            new insatiable(plugin).insatiableActive(player, event);


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