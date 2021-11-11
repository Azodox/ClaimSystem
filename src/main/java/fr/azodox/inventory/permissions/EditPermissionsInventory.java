package fr.azodox.inventory.permissions;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.azodox.inventory.util.FastInv;
import fr.azodox.util.HeadUtil;
import fr.azodox.util.ItemBuilder;
import fr.azodox.util.WGRegionUtil;

public class EditPermissionsInventory extends FastInv {

  public EditPermissionsInventory(Player player, ProtectedRegion region){
    super(5 * 9, ChatColor.YELLOW + "§lPermissions");

    var flags = WGRegionUtil.getFlags(region);
    int i = 0;
    for (var flag : flags.keySet()) {
          setItem(i, new ItemBuilder(HeadUtil.getHead("permission"))
            .setName(flag).build(), e -> {
              new ChooseStateInventory(player, this, region, flags.get(flag)).open(player);
            });
          i++;
    }
  }
}
