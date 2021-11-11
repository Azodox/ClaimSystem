package fr.azodox.inventory.permissions;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import fr.azodox.inventory.util.FastInv;
import fr.azodox.util.HeadUtil;
import fr.azodox.util.ItemBuilder;

public class ChooseStateInventory extends FastInv {

  public ChooseStateInventory(Player player, FastInv from, ProtectedRegion region, Flag flag){
    super(InventoryType.HOPPER, ChatColor.LIGHT_PURPLE + "§lChanger pour");

    setItem(4, new ItemBuilder(HeadUtil.getHead("back"))
      .setName(ChatColor.GRAY + "Retour").build(), e -> from.open(player));

    setItem(1, new ItemBuilder(Material.GREEN_CONCRETE)
      .setName(ChatColor.GREEN + "§lTout le monde").build(), e -> {
        region.setFlag(flag, StateFlag.State.ALLOW);
        new EditPermissionsInventory(player, region).open(player);
      });
    
    setItem(2, new ItemBuilder(Material.WHITE_CONCRETE)
      .setName(ChatColor.WHITE + "§lUniquement les Membres").build(), e -> {
        region.setFlag(flag, StateFlag.State.ALLOW);
        region.setFlag(flag.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
        new EditPermissionsInventory(player, region).open(player);
      });
  }
}
