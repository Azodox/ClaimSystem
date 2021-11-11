package fr.azodox.inventory;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.azodox.inventory.util.FastInv;
import fr.azodox.util.HeadUtil;
import fr.azodox.util.ItemBuilder;
import fr.azodox.util.WGRegionUtil;
import org.bukkit.ChatColor;

public class EditPermissionsInventory extends FastInv {

  public EditPermissionsInventory(ProtectedRegion region){
    super(5 * 9, ChatColor.YELLOW + "§lPermissions");

    var flags = WGRegionUtil.getFlags(region);
    for (int i = 0; i < flags.size(); i++) {
          var flag = flags.get(i);
          setItem(i, new ItemBuilder(HeadUtil.getHead("permission"))
            .setName(flag).build());
    }
  }
}
