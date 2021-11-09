package fr.azodox.inventory;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;

public interface CBlockInventory {

    void open(Player player, ProtectedRegion region);
}
