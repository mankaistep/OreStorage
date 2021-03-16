package me.manaki.plugin.orestorage.island;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import org.bukkit.entity.Player;

public class ASkyblockManager implements IslandManager {

    @Override
    public boolean isInOwnIsland(Player player) {
        com.wasteofplastic.askyblock.Island is = ASkyBlockAPI.getInstance().getIslandAt(player.getLocation());
        if (is == null) return false;
        return is.getMembers().contains(player.getUniqueId());
    }

}
