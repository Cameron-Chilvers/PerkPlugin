package perkplugin.perkplugin.classes;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.util.ConfigUtil;

import java.util.*;

import static java.lang.Float.parseFloat;

public class PlayerCustom {
    private final String[][] defaultAttributes = {
        {"GENERIC_MAX_HEALTH", "20.0"},
        {"GENERIC_KNOCKBACK_RESISTANCE", "0.0"},
        {"GENERIC_MOVEMENT_SPEED", "0.1"},
        {"GENERIC_ATTACK_DAMAGE", "8.0"},
        {"GENERIC_ATTACK_SPEED", "4.0"},
        {"GENERIC_ARMOR", "0.0"},
        {"GENERIC_ARMOR_TOUGHNESS", "0.0"},
        {"GENERIC_LUCK", "1.0"}
    };
    private HashMap<String, String> allPerks;
    public HashMap<String, Float> attributes;
    private HashMap<String, List<Float>> potionValues;
    private Player player;
    private String playerUUIDString;
    private PerkPlugin plugin;
    public PlayerCustom(Player player, PerkPlugin plugin){
        this.player = player;
        this.plugin = plugin;
        this.playerUUIDString = this.player.getUniqueId().toString();
        allPerks = new HashMap<>();

        populateAttributes(plugin);

        Bukkit.getLogger().info(attributes.toString());
    }

    // HELPER FUNCTIONS
    public void assignBuffs(){
        HashMap<String, Float> newAttributes = calculateAttributes();

        if(!newAttributes.equals(attributes)){
            double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double currPLayerHealth =  player.getHealth();

            // Loop through the new attributes and then set them to the player
            for(Map.Entry<String, Float> attribute : newAttributes.entrySet()){
                String attributeName = attribute.getKey();
                Float attributeValue = attribute.getValue();

               this.player.getAttribute(getAttributeByName(attributeName)).setBaseValue(attributeValue);

               // Add here to update player health without breaking everything
            }

            // Storing the new changes
            attributes = newAttributes;
        }

        HashMap<String, List<Float>> newPotions = calculatePotions();
        if(!newPotions.equals(potionValues)){
            //this.player.clearActivePotionEffects();
            Collection<PotionEffect> currentPotionEffects = this.player.getActivePotionEffects();
            for (PotionEffect potionEffect : currentPotionEffects){
                this.player.removePotionEffect(potionEffect.getType());
            }


            // Looping new potion values
            for(Map.Entry<String, List<Float>> potion : newPotions.entrySet()){
                String potionName = potion.getKey();
                int potionStrength = (int) potion.getValue().get(0).floatValue();
                int potionDuration = (int) potion.getValue().get(1).floatValue();

                // Creating and giving the effect
                PotionEffect potionEffect = new PotionEffect(getPotionEffectTypeByName(potionName),potionDuration*20, potionStrength-1, false, false );
                this.player.addPotionEffect(potionEffect);
            }

            potionValues = newPotions;
        }
    }


    // Helper function to
    private HashMap<String, Float> calculateAttributes(){
        HashMap<String, Float> newAttributes = new HashMap<>();

        // Setting up the newAttributes map
        for (String[] attribute : defaultAttributes) {
            newAttributes.put(attribute[0], parseFloat(attribute[1]));
        }

        // Adding the perk attributes to it
        for (String perkName : allPerks.keySet()){
            // Getting the players effects
            HashMap<String, List<Double>> playerEffects = plugin.perkMap.get(perkName).getPlayerBuffs(playerUUIDString);

            // Moving to next perk if problem
            if (playerEffects == null){
                continue;
            }

            for (Map.Entry<String, List<Double>> perkEffect : playerEffects.entrySet()){
                String effectName = perkEffect.getKey();
                Float effectStrength = perkEffect.getValue().get(0).floatValue();

                if(!newAttributes.containsKey(effectName)){
                    continue;
                }

                newAttributes.put(effectName, newAttributes.get(effectName) + effectStrength);
            }
        }
        Bukkit.getLogger().info("DEFAULT ATTRIBUTES");
        for(String[] perkEffect : defaultAttributes){
            Bukkit.getLogger().info("NAME: " + perkEffect[0] + ", VALUE: " + perkEffect[1]);
        }


        Bukkit.getLogger().info("NEW ATTRIBUTES");
        for(Map.Entry<String, Float> perkEffect : newAttributes.entrySet()){
            Bukkit.getLogger().info("NAME: " + perkEffect.getKey() + ", VALUE: " + perkEffect.getValue());
        }

        return newAttributes;
    }

    private HashMap<String, List<Float>> calculatePotions(){
        HashMap<String, List<Float>> newPotionValues = new HashMap<String, List<Float>>();

        // Adding the perk attributes to it
        for (String perkName : allPerks.keySet()){
            // Getting the players effects
            HashMap<String, List<Double>> playerEffects = plugin.perkMap.get(perkName).getPlayerBuffs(playerUUIDString);

            // Moving to next perk if problem
            if (playerEffects == null){
                continue;
            }

            for (Map.Entry<String, List<Double>> perkEffect : playerEffects.entrySet()){
                String effectName = perkEffect.getKey();
                Float effectStrength = perkEffect.getValue().get(0).floatValue();
                Float effectDuration = perkEffect.getValue().get(1).floatValue();

                // Only using potion effects
                if(effectName.contains("GENERIC")){
                    continue;
                }

                // If already exists go up one in strength
                if(newPotionValues.containsKey(effectName)){
                    newPotionValues.get(effectName).set(0, newPotionValues.get(effectName).get(0) + 1);

                    if(newPotionValues.get(effectName).get(1) < effectDuration){
                        newPotionValues.get(effectName).set(1, effectDuration);
                    }

                    continue;
                }

                // Temp list to be added
                List<Float> effectList = new ArrayList<>();
                effectList.add(effectStrength);
                effectList.add(effectDuration);

                newPotionValues.put(effectName, effectList);
            }
        }

        Bukkit.getLogger().info("ALL POTIONS");
        for(Map.Entry<String, List<Float>> perkEffect : newPotionValues.entrySet()){
            Bukkit.getLogger().info("NAME: " + perkEffect.getKey() + ", VALUE: " + perkEffect.getValue().get(0) + ", DURATION: " + perkEffect.getValue().get(1));
        }

        return newPotionValues;
    };


    private void populateAttributes(PerkPlugin plugin){
        // Populating attributes
        ConfigUtil configUtil = new ConfigUtil(plugin, "player_perks/" + playerUUIDString + ".yml");

        // Attributes not found add it
        if(!configUtil.getConfig().isConfigurationSection("Attributes")) {
            configUtil.getConfig().createSection("Attributes");
            configUtil.save();

            ConfigurationSection attributesConfigSection = configUtil.getConfig().getConfigurationSection("Attributes");

            for (String[] attribute : defaultAttributes) {
                attributesConfigSection.set(attribute[0], parseFloat(attribute[1]));
            }
            configUtil.save();
        }

        // Get perks and stuff and add to perk pointer
        attributes = new HashMap<>();

        for (String key : configUtil.getConfig().getConfigurationSection("Attributes").getKeys(false)) {
            Integer attributeValue = configUtil.getConfig().getConfigurationSection("Attributes").getInt(key);

            attributes.put(key, attributeValue.floatValue());
        }

        // Actually setting the attributes to the player
        assignAttributes();
    }

    // PLAYER UTIL
    public void saveToFile(){
        ConfigUtil playerConfigUtil = new ConfigUtil(plugin, "player_perks/" + playerUUIDString + ".yml");

        if(playerConfigUtil.getConfig().isConfigurationSection("Attributes")){

            attributes.forEach((key, value) -> {
                playerConfigUtil.getConfig().getConfigurationSection("Attributes").set(key, value);
            });

            playerConfigUtil.save();
        }
    }

    // SETTERS
    // Giving the player their attributes
    public void assignAttributes(){
        // Loops through the attributes map and then give them to the player
    }

    // Giving the player their potion effects
    public void assignPotionEffects(){
        // Loops through potion effects and then gives them to player
    }

    // Adding perk if it does not exist
    public void addPerk(String perkName, String perkItem){
        if (!allPerks.containsKey(perkName)) {
            allPerks.put(perkName, perkItem);
        }
    }

    // Removing perk if exists
    public void removePerk(String perkName){
        allPerks.remove(perkName);
    }


    // GETTERS
    public String getPerkItem(String perkName){
        return allPerks.get(perkName);
    }

    public Set<String> getAllPlayerPerksSet(){
        return allPerks.keySet();
    }

    public String getPlayerUUIDString(){
        return playerUUIDString;
    }

    // Checker if player has a perk
    public boolean playerHasPerk(String perkName){
        return allPerks.containsKey(perkName);
    }

    public Player getPlayer(){return this.player; }

    public List<String> getAllPerks(){
        return new ArrayList<>(allPerks.keySet());
    }

    // Extras
    private static Attribute getAttributeByName(String attributeName) {
        switch (attributeName.toUpperCase()) {
            case "GENERIC_MAX_HEALTH":
                return Attribute.GENERIC_MAX_HEALTH;
            case "GENERIC_KNOCKBACK_RESISTANCE":
                return Attribute.GENERIC_KNOCKBACK_RESISTANCE;
            case "GENERIC_MOVEMENT_SPEED":
                return Attribute.GENERIC_MOVEMENT_SPEED;
            case "GENERIC_ATTACK_DAMAGE":
                return Attribute.GENERIC_ATTACK_DAMAGE;
            case "GENERIC_ATTACK_SPEED":
                return Attribute.GENERIC_ATTACK_SPEED;
            case "GENERIC_ARMOR":
                return Attribute.GENERIC_ARMOR;
            case "GENERIC_ARMOR_TOUGHNESS":
                return Attribute.GENERIC_ARMOR_TOUGHNESS;
            case "GENERIC_LUCK":
                return Attribute.GENERIC_LUCK;
            default:
                return null;
        }
    }

    private static PotionEffectType getPotionEffectTypeByName(String effectName) {
        switch (effectName.toUpperCase()) {
            case "ABSORPTION":
                return PotionEffectType.ABSORPTION;
            case "BAD_OMEN":
                return PotionEffectType.BAD_OMEN;
            case "BLINDNESS":
                return PotionEffectType.BLINDNESS;
            case "CONDUIT_POWER":
                return PotionEffectType.CONDUIT_POWER;
            case "CONFUSION":
                return PotionEffectType.CONFUSION;
            case "DAMAGE_RESISTANCE":
                return PotionEffectType.DAMAGE_RESISTANCE;
            case "DARKNESS":
                return PotionEffectType.DARKNESS;
            case "DOLPHINS_GRACE":
                return PotionEffectType.DOLPHINS_GRACE;
            case "FAST_DIGGING":
                return PotionEffectType.FAST_DIGGING;
            case "FIRE_RESISTANCE":
                return PotionEffectType.FIRE_RESISTANCE;
            case "GLOWING":
                return PotionEffectType.GLOWING;
            case "HARM":
                return PotionEffectType.HARM;
            case "HEAL":
                return PotionEffectType.HEAL;
            case "HEALTH_BOOST":
                return PotionEffectType.HEALTH_BOOST;
            case "HERO_OF_THE_VILLAGE":
                return PotionEffectType.HERO_OF_THE_VILLAGE;
            case "HUNGER":
                return PotionEffectType.HUNGER;
            case "INCREASE_DAMAGE":
                return PotionEffectType.INCREASE_DAMAGE;
            case "INVISIBILITY":
                return PotionEffectType.INVISIBILITY;
            case "JUMP":
                return PotionEffectType.JUMP;
            case "LEVITATION":
                return PotionEffectType.LEVITATION;
            case "LUCK":
                return PotionEffectType.LUCK;
            case "NIGHT_VISION":
                return PotionEffectType.NIGHT_VISION;
            case "POISON":
                return PotionEffectType.POISON;
            case "REGENERATION":
                return PotionEffectType.REGENERATION;
            case "SATURATION":
                return PotionEffectType.SATURATION;
            case "SLOW":
                return PotionEffectType.SLOW;
            case "SLOW_DIGGING":
                return PotionEffectType.SLOW_DIGGING;
            case "SLOW_FALLING":
                return PotionEffectType.SLOW_FALLING;
            case "SPEED":
                return PotionEffectType.SPEED;
            case "UNLUCK":
                return PotionEffectType.UNLUCK;
            case "WATER_BREATHING":
                return PotionEffectType.WATER_BREATHING;
            case "WEAKNESS":
                return PotionEffectType.WEAKNESS;
            case "WITHER":
                return PotionEffectType.WITHER;
            default:
                return null;
        }
    }
}
