package perkplugin.perkplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// THIS IS A TEST
public class Fly implements CommandExecutor {
    @Override
    public  boolean onCommand(CommandSender sender, Command command, String Label, String [] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("Players ONLy");
            return true;
        }

        Player player = (Player) sender;

        if(player.getAllowFlight()){
            player.setAllowFlight(false);
            player.sendMessage("NO FLYING");
        }else{
            player.setAllowFlight(true);
            player.sendMessage("FLYTIME");
        }

        return true;
    }
}