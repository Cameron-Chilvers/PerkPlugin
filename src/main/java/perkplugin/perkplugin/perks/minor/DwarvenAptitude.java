package perkplugin.perkplugin.perks.minor;

import org.bukkit.entity.Player;
import perkplugin.perkplugin.PerkPlugin;
import perkplugin.perkplugin.perks.Perks;

public class DwarvenAptitude extends Perks {

    public final String perkName = "dwarven-aptitude";
    private PerkPlugin plugin;

    public DwarvenAptitude(PerkPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void giveBuff(Player player) {
        int yLocation = player.getLocation().getBlockY();

        Boolean playerUnder50 = false;
        if (yLocation < 50){
            playerUnder50 = true;
        }

        plugin.perkMap.get(perkName).changePlayerType(player.getUniqueId().toString(), playerUnder50, plugin);

    }

    @Override
    public String getName() {
        return perkName;
    }
}
