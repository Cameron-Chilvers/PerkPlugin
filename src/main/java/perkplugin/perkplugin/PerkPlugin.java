package perkplugin.perkplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import perkplugin.perkplugin.commands.PerkSelector;
import perkplugin.perkplugin.handlers.PlayerHandler;
import perkplugin.perkplugin.perks.minor.lastInventory;
import perkplugin.perkplugin.runables.isInWaterChecker;
import perkplugin.perkplugin.runables.nightTimeChecker;
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

        // Creating the commands
        getCommand("fly").setExecutor(new Fly());
        getCommand("perks").setExecutor(new PerkSelector(this));
        getCommand("lastInventory").setExecutor(new lastInventory(this));

        // Creating objects that are not used??
        new PlayerHandler(this);
        new DelayedTask(this);

        // Starting the runnables
        new nightTimeChecker(this).nightTimeChecker();
        new isInWaterChecker(this).inWaterChecker();
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
}
