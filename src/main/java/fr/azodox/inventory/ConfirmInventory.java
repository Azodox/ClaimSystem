package fr.azodox.inventory;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.azodox.ClaimSystem;
import fr.azodox.inventory.util.FastInv;
import fr.azodox.util.ItemBuilder;
import fr.azodox.util.WGRegionUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class ConfirmInventory extends FastInv {

  private final FastInv from;
  private final ProtectedRegion region;
  private final ConfirmationReason reason;

  public ConfirmInventory(FastInv from, ProtectedRegion region, ConfirmationReason reason){
    super(InventoryType.HOPPER, ChatColor.YELLOW + "" + ChatColor.BOLD + "Confirmation");
    this.from = from;
    this.region = region;
    this.reason = reason;

    setItem(1, new ItemBuilder(Material.GREEN_DYE)
      .setName(ChatColor.GREEN + "§lConfirmer")
      .setLore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                          ",
              ChatColor.GRAY + "Attention : Cette action est irréversible.",
              ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                          ") 
      .build());

    setItem(2, new ItemBuilder(Material.PAPER)
      .setName(ChatColor.AQUA + "Êtes-vous sûr ?")
      .setLore(ChatColor.GRAY + "De vouloir §l" + reason).build());

    setItem(3, new ItemBuilder(Material.RED_DYE)
            .setName(ChatColor.RED + "§lAnnuler").build());
  }

  @Override
  protected void onClick(InventoryClickEvent event) {
    assert event.getCurrentItem() != null;
    var player = (Player) event.getWhoClicked();
    var item = event.getCurrentItem();
    var world = Bukkit.getWorld("world");

    if(item.getType().equals(Material.GREEN_DYE)){

      switch(this.reason){
        case DELETE:
          RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
          RegionManager regions = container.get(BukkitAdapter.adapt(world));

          //TODO : delete the gold block when deleting region
          Location center = WGRegionUtil.getRegionCenter(region, world);
          world.getBlockAt(center).setType(Material.AIR);

          regions.removeRegion(region.getId(), RemovalStrategy.REMOVE_CHILDREN);
          player.closeInventory();
          player.sendMessage(ClaimSystem.PLUGIN_PREFIX + "Votre claim " + WGRegionUtil.getRegionIndex(region.getId()) + " a été supprimé !");
          break;
      default:
        break;
      }

    }else if(item.getType().equals(Material.RED_DYE)){
      player.closeInventory();
      player.sendMessage(ClaimSystem.PLUGIN_PREFIX + "Vous avez annulé votre décision.");
      from.open(player);
    }

    event.setCancelled(true);
  }
}
