package perkplugin.perkplugin.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import perkplugin.perkplugin.PerkPlugin;

import java.util.List;

public class ListPerks implements CommandExecutor {
    private PerkPlugin plugin;

    public ListPerks(PerkPlugin plugin ){
        this.plugin = plugin;
    }

    @Override
    public  boolean onCommand(CommandSender sender, Command command, String Label, String [] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("Players ONLy");
            return true;
        }

        Player player = (Player) sender;

        List<String> perks = plugin.playerMap.get(player.getUniqueId().toString()).getAllPerks();

        player.sendMessage("Your Perks");
        for(String perk : perks){
            player.sendMessage(perk);
        }


        return true;
    }
}