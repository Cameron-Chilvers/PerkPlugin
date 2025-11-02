package perkplugin.perkplugin.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.classes.PerkCustom;
import perkplugin.perkplugin.classes.PlayerCustom;

import java.util.ArrayList;
import java.util.HashMap;


public class PermissionUtil {
    // perk.prized-possessions
    private HashMap<String, PerkCustom> perkMap;
    private static PerkPlugin plugin;
    public static HashMap<String, PlayerCustom> playerMap;
    public PermissionUtil(PerkPlugin plugin){
        this.plugin = plugin;
        this.perkMap = plugin.perkMap;
        this.playerMap = plugin.playerMap;
    }

    public static boolean checkForPermission(Player player, String permission){
        return plugin.perkMap.containsKey(permission) && plugin.perkMap.get(permission).containsPlayer(player.getUniqueId().toString());
    }


    // Adding the player to the perks in the configFile
    private void addToConfigFile(String perk, ConfigUtil configUtil, String playerUUIDString){
        // Checking if the perks section is in the config file
        if(configUtil.getConfig().getConfigurationSection("Perks") == null){
            configUtil.getConfig().createSection("Perks");
            configUtil.save();
        }

        ConfigurationSection perksConfigSection = configUtil.getConfig().getConfigurationSection("Perks");

        // Check if perk in the configFile
        if(!perksConfigSection.contains(perk)){
            // Add the perk
            perksConfigSection.createSection(perk);
            perksConfigSection.set(perk, new ArrayList<>(perkMap.get(perk).returnAllPlayers()));

            configUtil.save();
            return;
        }

        if(!perksConfigSection.getStringList(perk).contains(playerUUIDString)) {
            perksConfigSection.set(perk, new ArrayList<>(perkMap.get(perk).returnAllPlayers()));

            configUtil.save();
        }
    }

    // Using this to add the player perk file
    private void addToPlayerPerkFile(Player player, String perk, String playerUUIDString){
        ConfigUtil playerConfigUtil = new ConfigUtil(plugin, "player_perks/" + player.getUniqueId() + ".yml");

        if(playerConfigUtil.getConfig().isConfigurationSection("Perks")){
            // add perk to the list here
            if(!playerConfigUtil.getConfig().getConfigurationSection("Perks").contains(perk)){
                // Addint the perk and its item
                playerConfigUtil.getConfig().set("Perks."+perk, playerMap.get(playerUUIDString).getPerkItem(perk));
            }
        }else{
            // create file and add the dict
            for (String key : playerMap.get(playerUUIDString).getAllPlayerPerksSet()) {
                String value = playerMap.get(playerUUIDString).getPerkItem(key);

                playerConfigUtil.getConfig().set("Perks."+key, value);
            }
        }

        playerConfigUtil.save();
    }

    // Used to call the function with three parameters
    public void addPermission(Player player, String perk) {
        addPermission(player, perk, ""); // Calls the version with three parameters, passing an empty string as the default value
    }

    public void addPermission(Player player, String perk, String addition){
        ConfigUtil configUtil = new ConfigUtil(plugin, "config.yml");
        String playerUUIDString = player.getUniqueId().toString();

        // Get the 'Perks' section from the config

        // Check if permission exists in HashMap
        if(!perkMap.containsKey(perk)){
            perkMap.put(perk, new PerkCustom(perk));
            perkMap.get(perk).addPlayerToPerk(playerUUIDString);
        }

        // Check if playing in the permissions Set
        if(!perkMap.get(perk).containsPlayer(playerUUIDString)){
            perkMap.get(perk).addPlayerToPerk(playerUUIDString);
        }

        // Add to player perk map
        playerMap.get(playerUUIDString).addPerk(perk, addition);


        // Helper function defined above
        addToConfigFile(perk, configUtil, playerUUIDString);
        addToPlayerPerkFile(player, perk, playerUUIDString);
    }

    public void removePermission(Player player, String perk) {
        ConfigUtil configUtil = new ConfigUtil(plugin, "config.yml");
        String playerUUIDString = player.getUniqueId().toString();

        // Get the 'Perks' section from the config
        ConfigurationSection perksConfig = configUtil.getConfig().getConfigurationSection("Perks");

        // Checks if perk exists
        if (!perkMap.containsKey(perk)) {
            return;
        }

        // Checks if player in the perk
        if (!perkMap.get(perk).containsPlayer(playerUUIDString)) {
            return;
        }

        // Removing from set and updating the config file
        perkMap.get(perk).removePlayerFromPerk(playerUUIDString);
        perksConfig.set(perk, new ArrayList<>(perkMap.get(perk).returnAllPlayers()));
        configUtil.save();

        if (playerMap.containsKey(playerUUIDString) && playerMap.get(playerUUIDString).playerHasPerk(perk)) {
            playerMap.get(playerUUIDString).removePerk(perk);
            ConfigUtil playerPerkConfigUtil = new ConfigUtil(plugin, "player_perks/" + player.getUniqueId() + ".yml");
            // Setting to null deletes from the yml
            playerPerkConfigUtil.getConfig().set("Perks." + perk, null);

            playerPerkConfigUtil.save();
        }

        plugin.playerMap.get(playerUUIDString).assignBuffs();
    }
}