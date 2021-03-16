package me.manaki.plugin.orestorage.island;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.entity.Player;

public class SuperiorSkyblock2Manager implements IslandManager {

    @Override
    public boolean isInOwnIsland(Player player) {
        Island is = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
        if (is == null) return false;
        return is.getIslandMembers(true).contains(SuperiorSkyblockAPI.getPlayer(player.getUniqueId()));
    }

}
