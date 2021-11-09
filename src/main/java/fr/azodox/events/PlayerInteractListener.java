package fr.azodox.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.azodox.ClaimSystem;
import fr.azodox.blocks.ICBlock;
import fr.azodox.util.CBlockUtil;
import fr.azodox.util.WGRegionUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = e.getClickedBlock();
            assert block != null;
            if(block.getType() == Material.GOLD_BLOCK){
                if(CBlockUtil.isAClaimBlock(block)) {
                    ProtectedRegion region = WGRegionUtil.getRegionAt(block.getLocation());
                    if (!region.getOwners().contains(player.getUniqueId())) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ClaimSystem.PLUGIN_PREFIX + "Vous devez être le propriétaire de cette zone pour ouvrir la configuration."));
                        return;
                    }

                    EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
                    PacketPlayOutAnimation packet = new PacketPlayOutAnimation(entityPlayer, 0);
                    entityPlayer.b.sendPacket(packet);

                    ICBlock cBlock = CBlockUtil.getClaimBlockFromLevel(CBlockUtil.getClaimBlockLevel(block));
                    cBlock.getInventory().open(player, region);
                }
                e.setCancelled(true);
            }
        }
    }
}
