package perkplugin.perkplugin.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import perkplugin.perkplugin.PerkPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PermissionUtil {
    // perk.prized-possessions
    private HashMap<String, Set<String>> playerPermissions;
    private static PerkPlugin plugin;
    public static HashMap<String, HashMap<String, String>> perkPointer;
    public PermissionUtil(PerkPlugin plugin){
        this.plugin = plugin;
        this.playerPermissions = plugin.playerPermissions;
        this.perkPointer = plugin.perkPointer;
    }

    public static boolean checkForPermission(Player player, String permission){
        return plugin.playerPermissions.containsKey(permission) && plugin.playerPermissions.get(permission).contains(player.getUniqueId().toString());
    }


    // Adding the player to the perks in the configFile
    private void addToConfigFile(String perk, ConfigUtil configUtil, String playerUUIDString){
        // Checking if the perks section is in the config file
        if(configUtil.getConfig().getConfigurationSection("Perks") == null){
            configUtil.getConfig().createSection("Perks");
            configUtil.save();
        }

        ConfigurationSection perksConfig = configUtil.getConfig().getConfigurationSection("Perks");

        // Check if perk in the configFile
        if(!perksConfig.contains(perk)){
            // Add the perk
            perksConfig.createSection(perk);
            perksConfig.set(perk, new ArrayList<>(playerPermissions.get(perk)));

            configUtil.save();
            return;
        }

        if(!perksConfig.getStringList(perk).contains(playerUUIDString)) {
            perksConfig.set(perk, new ArrayList<>(playerPermissions.get(perk)));

            configUtil.save();
        }
    }

    // Using this to add the player perk file
    private void addToPlayerPerkFile(Player player, String perk, String playerUUIDString){
        ConfigUtil playerPerkConfigUtil = new ConfigUtil(plugin, "player_perks/" + player.getUniqueId() + ".yml");

        if(playerPerkConfigUtil.getConfig().isConfigurationSection("Perks")){
            // add perk to the list here
            if(!playerPerkConfigUtil.getConfig().getConfigurationSection("Perks").contains(perk)){
                playerPerkConfigUtil.getConfig().set("Perks."+perk, perkPointer.get(playerUUIDString).get(perk));
            }
        }else{
            // create file and add the dict
            for (String key : perkPointer.get(playerUUIDString).keySet()) {
                String value = perkPointer.get(playerUUIDString).get(key);

                playerPerkConfigUtil.getConfig().set("Perks."+key, value);
            }
        }

        playerPerkConfigUtil.save();
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
        if(!playerPermissions.containsKey(perk)){
            playerPermissions.put(perk, new HashSet<String>());
            playerPermissions.get(perk).add(playerUUIDString);
        }

        // Check if playing in the permissions Set
        if(!playerPermissions.get(perk).contains(playerUUIDString)){
            playerPermissions.get(perk).add(playerUUIDString);
        }

        // Add to player perk map
        if(!perkPointer.containsKey(playerUUIDString)){
            perkPointer.put(playerUUIDString, new HashMap<String, String>());
        }

        // Add to player perk map
        if(!perkPointer.get(playerUUIDString).containsKey(perk)){
            perkPointer.get(playerUUIDString).put(perk, addition);
        }

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
        if (!playerPermissions.containsKey(perk)) {
            return;
        }

        // Checks if player in the perk
        if (!playerPermissions.get(perk).contains(playerUUIDString)) {
            return;
        }

        // Removing from set and updating the config file
        playerPermissions.get(perk).remove(playerUUIDString);
        perksConfig.set(perk, new ArrayList<>(playerPermissions.get(perk)));
        configUtil.save();

        if (perkPointer.containsKey(playerUUIDString) && perkPointer.get(playerUUIDString).containsKey(perk)) {
            perkPointer.get(playerUUIDString).remove(perk);
            ConfigUtil playerPerkConfigUtil = new ConfigUtil(plugin, "player_perks/" + player.getUniqueId() + ".yml");
            // Setting to null deletes from the yml
            playerPerkConfigUtil.getConfig().set("Perks." + perk, null);

            playerPerkConfigUtil.save();
        }
    }
}