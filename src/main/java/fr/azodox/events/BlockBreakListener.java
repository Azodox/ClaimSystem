package fr.azodox.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.azodox.ClaimSystem;
import fr.azodox.util.WGRegionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Block block = e.getBlock();

        if(block.getType() != Material.GOLD_BLOCK){
            return;
        }

        Location location = block.getLocation();
        if(WGRegionUtil.isInARegion(location)){
            World world = block.getLocation().getWorld();

            ProtectedRegion region = WGRegionUtil.getRegionAt(location);
            Location point1 = WGRegionUtil.toBukkitLocation(world, region.getMinimumPoint());
            Location point2 = WGRegionUtil.toBukkitLocation(world, region.getMaximumPoint());

            int y = location.getBlockY();

            point1.setY(y);
            point2.setY(y);

            Location center = WGRegionUtil.getPointsCenter(point1, point2);

            if(!location.equals(center)){
                return;
            }

            if(region.getId().contains("claim")){
                e.getPlayer().sendMessage(ClaimSystem.PLUGIN_PREFIX + "Vous ne pouvez pas casser votre bloc d'or central.");
                e.setCancelled(true);
            }
        }
    }
}
