package fr.azodox.inventory;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import fr.azodox.ClaimSystem;
import fr.azodox.inventory.util.FastInv;
import fr.azodox.util.ItemBuilder;
import fr.azodox.util.WGRegionUtil;
import net.md_5.bungee.api.ChatColor;

public class ConfirmInventory extends FastInv {

  private final FastInv from;
  private final ProtectedRegion region;
  private final ConfirmationReason reason;

  public ConfirmInventory(FastInv from, ProtectedRegion region, ConfirmationReason reason){
    super(InventoryType.HOPPER, ChatColor.RED + "" + ChatColor.BOLD + "Confirmation");
    this.from = from;
    this.region = region;
    this.reason = reason;

    setItem(2, new ItemBuilder(Material.GREEN_DYE)
      .setName(ChatColor.GREEN + "Souhaitez-vous confirmer votre décision ?")
      .setLore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                          ",
              ChatColor.GRAY + "Attention : Cette action est irréversible.",
              ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                          ") 
      .build());

    setItem(3, new ItemBuilder(Material.RED_DYE)
      .setName(ChatColor.GRAY + "Êtes-vous sûr ?")
      .setLore(ChatColor.AQUA + "De vouloir §l" + reason).build());
  }

  @Override
  protected void onClick(InventoryClickEvent event) {
    assert event.getCurrentItem() != null;
    var player = (Player) event.getWhoClicked();
    var item = event.getCurrentItem();

    if(item.getType().equals(Material.GREEN_DYE)){

      switch(this.reason){
        case DELETE:
          RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
          RegionManager regions = container.get(BukkitAdapter.adapt(Bukkit.getWorld("world")));
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
