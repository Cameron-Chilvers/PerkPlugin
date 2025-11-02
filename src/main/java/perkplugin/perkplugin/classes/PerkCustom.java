package perkplugin.perkplugin.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import perkplugin.perkplugin.PerkPlugin;

import java.util.*;

public class PerkCustom {
    private final HashMap<String, List<Double>> proBuffs;
    private final HashMap<String, List<Double>> antiBuffs;
    private final HashMap<String, Boolean> players;
    private final String perkName;

    public PerkCustom(String perkName){
        this.perkName = perkName;

        this.proBuffs = new HashMap<>();
        this.antiBuffs = new HashMap<>();
        this.players = new HashMap<>();
    }

    public void addProBuff(String buffName, List<Double> buffInformation){
        this.proBuffs.put(buffName, buffInformation);
    }

    public void addAntiBuff(String buffName, List<Double> buffInformation){
        this.antiBuffs.put(buffName, buffInformation);
    }

    public void addPlayerToPerk(String playerUUID){
        this.players.put(playerUUID, null);
    }

    public void addPlayersToPerk(HashSet<String> playerList){
        for (String playerUUID : playerList){
            this.players.put(playerUUID, null);
        }
    }

    public void removePlayerFromPerk(String playerUUID){
        if(containsPlayer(playerUUID)){
            this.players.remove(playerUUID);
        }
    }

    public Set<String> returnAllPlayers(){
        return this.players.keySet();
    }

    public boolean containsPlayer(String playerUUID) {
        return this.players.containsKey(playerUUID);
    }

    public String getPerkName(){
        return this.perkName;
    }

    public void changePlayerType(String playerUUID, Boolean newType, PerkPlugin plugin){
        if(this.players.containsKey(playerUUID)) {
            this.players.put(playerUUID, newType);

            try{
                // Actually assigning the buffs;
                plugin.playerMap.get(playerUUID).assignBuffs();
            } catch(Exception e) {
                Bukkit.getLogger().info(ChatColor.YELLOW + "Player was removed at a bad time!");
            }


        }
    }

    public HashMap<String, List<Double>> getPlayerBuffs(String playerUUID){
        // Returning null if player does not have perk or if it is null
        if(!this.players.containsKey(playerUUID) || this.players.get(playerUUID) == null){
            return null;
        }

        // returning pro buffs if is true
        if(this.players.get(playerUUID)){
            return new  HashMap<>(proBuffs);
        }

        // Last case has to be false so returning anti buffs
        return new HashMap<>(antiBuffs);
    }


    public void printProBuffs() {
        Bukkit.getLogger().info("PRO BUFFS");
        for (Map.Entry<String, List<Double>> effects : this.proBuffs.entrySet()) {
            Bukkit.getLogger().info("Buff Name: " + effects.getKey() + ", Strength: " +effects.getValue().get(0)+ ", Duration: " +effects.getValue().get(1));
        }
    }
    public void printAntiBuffs() {
        Bukkit.getLogger().info("ANTI BUFFS");
        for (Map.Entry<String, List<Double>> effects : this.antiBuffs.entrySet()) {
            Bukkit.getLogger().info("Buff Name: " + effects.getKey() + ", Strength: " +effects.getValue().get(0)+ ", Duration: " +effects.getValue().get(1));
        }
    }
}
