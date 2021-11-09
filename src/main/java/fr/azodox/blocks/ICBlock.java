package fr.azodox.blocks;

import fr.azodox.inventory.CBlockInventory;
import org.bukkit.inventory.ItemStack;

public interface ICBlock {

    String getName();
    ItemStack getItem(String player);
    String getLevel(boolean letter);
    double getXSize();
    double getZSize();
    CBlockInventory getInventory();
}
