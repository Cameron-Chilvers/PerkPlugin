package perkplugin.perkplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import perkplugin.perkplugin.commands.ListPerks;
import perkplugin.perkplugin.commands.PerkSelector;
import perkplugin.perkplugin.handlers.PlayerHandler;
import perkplugin.perkplugin.classes.PerkCustom;
import perkplugin.perkplugin.classes.PlayerCustom;
import perkplugin.perkplugin.perks.minor.PrizedPossessions;
import perkplugin.perkplugin.runables.SchedulerManager;
import perkplugin.perkplugin.util.*;
import perkplugin.perkplugin.commands.Fly;

import java.util.*;

public final class PerkPlugin extends JavaPlugin {
    public HashMap<String, PlayerCustom> playerMap = new HashMap<String, PlayerCustom>();
    public HashMap<String, PerkCustom> perkMap = new HashMap<String, PerkCustom>();


    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        // Adding all perks to the perkMap and setting them up
        getPerkEffects();

        // Populates playerPermissions HashMap
        setUpPlayerPerks();

        // Creating the commands
        getCommand("fly").setExecutor(new Fly());
        getCommand("perks").setExecutor(new PerkSelector(this));
        getCommand("lastInventory").setExecutor(new PrizedPossessions(this));
        getCommand("listPerks").setExecutor(new ListPerks(this));

        // Creating objects that are not used??
        new PlayerHandler(this);
        new DelayedTask(this);

        // Starting the runnables
        SchedulerManager schedulerManager = new SchedulerManager(this);
        schedulerManager.EverySecondRunnable();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Shut Down");
        playerMap.clear();
        perkMap.clear();
    }

    private void setUpPlayerPerks(){
        for(String perkName : this.getConfig().getConfigurationSection("Perks").getKeys(false)){
            List<String> playerList = this.getConfig().getStringList("Perks." + perkName);

            // Adding list of players to the perkMap from the file
            if(perkMap.containsKey(perkName)){
                perkMap.get(perkName).addPlayersToPerk(new HashSet<>(playerList));
            }else{
                perkMap.put(perkName, new PerkCustom(perkName));
                perkMap.get(perkName).addPlayersToPerk(new HashSet<>(playerList));
            }

        }
    }

    private void getPerkEffects(){
        // Looping through the config file and adding info to the hash sets
        for(String perk : this.getConfig().getConfigurationSection("effects").getKeys(false)) {

            // Adding every perk
            perkMap.put(perk, new PerkCustom(perk));

            // Looping through both pro and anti sections
            for(String type : this.getConfig().getConfigurationSection("effects." + perk).getKeys(false)){

                // Looping through the effects
                for(String effect : this.getConfig().getConfigurationSection("effects." + perk + "." + type).getKeys(false)){
                    // Getting the strength and duration
                    List<Double> file_information = this.getConfig().getDoubleList("effects." + perk + "." + type + "." +effect);

                    // Adding the pro and the anti information from the yaml file
                    if(type.equals("pro")){
                        perkMap.get(perk).addProBuff(effect, file_information);
                    }else{
                        perkMap.get(perk).addAntiBuff(effect, file_information);
                    }

                }
            }
        }

        // Printing the information
        for (Map.Entry<String, PerkCustom> entry1 : perkMap.entrySet()) {
            PerkCustom currPerk = entry1.getValue();
            Bukkit.getLogger().info("Perks " + currPerk.getPerkName());
            currPerk.printProBuffs();
            currPerk.printAntiBuffs();
        }

    }
}
