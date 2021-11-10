package fr.azodox.blocks;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ICBlock {

    String getName();
    ItemStack getItem(String player);
    String getLevel(boolean letter);
    double getXSize();
    double getZSize();
    void openInventory(Player player, ProtectedRegion region);
}
