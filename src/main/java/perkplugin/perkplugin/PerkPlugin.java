package perkplugin.perkplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import perkplugin.perkplugin.commands.PerkSelector;
import perkplugin.perkplugin.handlers.PlayerHandler;
import perkplugin.perkplugin.perks.major.vampirism;
import perkplugin.perkplugin.perks.minor.lastInventory;
import perkplugin.perkplugin.util.*;
import perkplugin.perkplugin.commands.Fly;

import java.util.*;

import static perkplugin.perkplugin.util.buffUtil.getPlayerFromUUIDString;

public final class PerkPlugin extends JavaPlugin {
    public HashMap<String, Set<String>> playerPermissions = new HashMap<String, Set<String>>();
    public HashMap<String, HashMap<String, String>> perkPointer = new HashMap<String, HashMap<String, String>>();

    // effects = {perk: {pro: {effectName: [strength, duration]}, anit: {effectName: [strength, duration]}} , anotherPerk: .... }
    public static HashMap<String, ArrayList<ArrayList<String>>> perkBuffs = new HashMap<String, ArrayList<ArrayList<String>>>();
    private buffUtil buffUtil = new buffUtil();

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        getPerkEffects();

        // Populates playerPermissions HashMap
        setUpPlayerPermissions();

        getCommand("fly").setExecutor(new Fly());
        getCommand("perks").setExecutor(new PerkSelector(this));
        getCommand("lastInventory").setExecutor(new lastInventory(this));

        new PlayerHandler(this);
        new PlayerHandler(this);
        new DelayedTask(this);

        // Starting the runnables
        nightTimeChecker();
        inWaterChecker();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Shut Down");
        playerPermissions.clear();
    }

    private void setUpPlayerPermissions(){
        for(String permission : this.getConfig().getConfigurationSection("Perks").getKeys(false)){
            List<String> playerList = this.getConfig().getStringList("Perks." + permission);

            playerPermissions.put(permission, new HashSet<>(playerList));
        }
    }

    private void getPerkEffects(){
        ArrayList<Object> test = new ArrayList<>();

        for(String perk : this.getConfig().getConfigurationSection("effects").getKeys(false)) {
            Bukkit.getLogger().info(perk);
            for(String type : this.getConfig().getConfigurationSection("effects." + perk).getKeys(false)){
                Bukkit.getLogger().info(type);
                for(String effect : this.getConfig().getConfigurationSection("effects." + perk + "." + type).getKeys(false)){
                    Bukkit.getLogger().info(effect + " FOR " + this.getConfig().getStringList("effects." + perk + "." + type + "." +effect).toString());
                }

            }
        }
    }
    private void nightTimeChecker(){
        new BukkitRunnable() {
            @Override
            public void run() {
                vampirism vampirism = new vampirism();

                for (World world : Bukkit.getWorlds()) {
                    if(world.getName().equals("world")) {
                        if (NightDetector.isNight(world) && playerPermissions.containsKey("vampirism")) {
                            // Night has started in this world, do something
                            for (String playerUUID : playerPermissions.get("vampirism")) {
                                if (perkPointer.containsKey(playerUUID)) {
                                    // Vamp Buffs
                                    vampirism.vampBuff(playerUUID, true);
                                }
                            }
                        } else if (NightDetector.isDay(world) && playerPermissions.containsKey("vampirism")) {
                            for (String playerUUID : playerPermissions.get("vampirism")) {
                                // Vamp Debuffs
                                if (perkPointer.containsKey(playerUUID)) {
                                    vampirism.vampBuff(playerUUID, false);
                                }
                            }
                        }
                    }
                }
            }
            // this runs every min
        }.runTaskTimer(this, 0, 20*30); // Run every 30 seconds
    }

    private void inWaterChecker(){
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if(playerPermissions.containsKey("fishy-boi")) {
                for (String playerStr : playerPermissions.get("fishy-boi")) {
                    Player player = getPlayerFromUUIDString(playerStr);
                    if(perkPointer.containsKey(playerStr)) {
                        applyBreathingEffect(player);
                    }
                }
            }
        }, 0, 20 * 3); // Run every 1 second (20 ticks)
    }

    private void applyBreathingEffect(Player player) {
        Location playerLocation = player.getLocation();
        Block blockUnderPlayer = playerLocation.getBlock().getRelative(0, -1, 0);
        Block blockAtPlayer = playerLocation.getBlock();

        if (player.isSwimming() || blockUnderPlayer.getType() == Material.WATER || blockAtPlayer.getType() == Material.WATER || blockUnderPlayer.getType() == Material.LAVA || blockAtPlayer.getType() == Material.LAVA) {
            if(blockUnderPlayer.getType() == Material.LAVA || blockAtPlayer.getType() == Material.LAVA){
                buffUtil.giveBuff(player, PotionEffectType.FIRE_RESISTANCE, 60, 5);
            }

            buffUtil.giveBuff(player, PotionEffectType.WATER_BREATHING, 30, 1);
            buffUtil.giveBuff(player, PotionEffectType.SPEED, 30, 2);
            buffUtil.giveBuff(player, PotionEffectType.DOLPHINS_GRACE, 30, 1);
            buffUtil.giveBuff(player, PotionEffectType.CONDUIT_POWER, 30, 5);
            buffUtil.giveBuff(player, PotionEffectType.INCREASE_DAMAGE, 30, 2);
            buffUtil.giveBuff(player, PotionEffectType.FAST_DIGGING, 30, 3);
        } else {
            buffUtil.giveBuff(player, PotionEffectType.HARM, 1, 1);
        }
    }
}
