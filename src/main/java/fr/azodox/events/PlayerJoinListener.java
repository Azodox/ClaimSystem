package fr.azodox.events;

import fr.azodox.ClaimSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final ClaimSystem claimSystem;

    public PlayerJoinListener(ClaimSystem claimSystem) {
        this.claimSystem = claimSystem;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        /*if(!player.hasPlayedBefore()){
            CBlockLI cBlockLI = new CBlockLI();
            player.getInventory().addItem(cBlockLI.getItem(player.getName()));
        }*/
        player.getInventory().setItem(0, claimSystem.getClaimBlock(1).getItem(player.getName()));

    }
}
