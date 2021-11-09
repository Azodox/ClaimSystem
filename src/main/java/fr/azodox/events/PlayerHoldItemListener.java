package fr.azodox.events;

import fr.azodox.ClaimSystem;
import fr.azodox.gui.CBlockGUI;
import fr.azodox.util.CBlockUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerHoldItemListener implements Listener {
    
    private final ClaimSystem main;

    public PlayerHoldItemListener(ClaimSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onHoldItem(PlayerItemHeldEvent e){
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItem(e.getNewSlot());
        ItemStack from = player.getInventory().getItem(e.getPreviousSlot());

        if(from == null) return;

        if(from.hasItemMeta() && from.getItemMeta().getDisplayName().contains("§e§lBloc de claim")){
            CBlockGUI.removeGUI(player);

            main.getFakesParticles().remove(player.getUniqueId());
        }

        if(item == null) return;
        if(item.hasItemMeta() && item.getItemMeta().getDisplayName().contains("§e§lBloc de claim")){
            CBlockGUI cBlockGUI = new CBlockGUI(player, main);
            cBlockGUI.show(main.getClaimBlock(CBlockUtil.getLevel(item)), false);
        }
    }
}
