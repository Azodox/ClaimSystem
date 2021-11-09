package fr.azodox.events;

import fr.azodox.ClaimSystem;
import fr.azodox.gui.CBlockGUI;
import fr.azodox.util.BossBar;
import fr.azodox.util.CBlockUtil;
import fr.azodox.util.WGRegionUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMoveListener implements Listener {

    private final ClaimSystem main;

    public PlayerMoveListener(ClaimSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player player = e.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.hasItemMeta() && item.getItemMeta().getDisplayName().contains("§e§lBloc de claim")){
            CBlockGUI cBlockGUI = new CBlockGUI(player, main);
            cBlockGUI.show(main.getClaimBlock(CBlockUtil.getLevel(item)), true);

            if(BossBar.getBossBar(player) != null){
                if(WGRegionUtil.getRegionsNumber(player.getLocation().getWorld(), player) < ClaimSystem.MAX_CLAIMS){
                    BossBar.removeBossBar(player.getUniqueId());
                }
            }
        }
    }
}
