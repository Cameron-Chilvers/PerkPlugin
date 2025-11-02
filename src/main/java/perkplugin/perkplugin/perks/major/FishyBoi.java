package perkplugin.perkplugin.perks.major;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.perks.Perks;

public class FishyBoi extends Perks {
    public final String perkName = "fishy-boi";
    private PerkPlugin plugin;
    public FishyBoi(PerkPlugin plugin){
        this.plugin = plugin;
    }
    @Override
    public void giveBuff(Player player){
        Location playerLocation = player.getLocation();
        Block blockUnderPlayer = playerLocation.getBlock().getRelative(0, -1, 0);
        Block blockAtPlayer = playerLocation.getBlock();

        boolean inWater = player.isSwimming() || blockUnderPlayer.getType() == Material.WATER || blockAtPlayer.getType() == Material.WATER || blockUnderPlayer.getType() == Material.LAVA || blockAtPlayer.getType() == Material.LAVA;

        plugin.perkMap.get(perkName).changePlayerType(player.getUniqueId().toString(), inWater, plugin);

    }


}
