package fr.azodox.util;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.azodox.ClaimSystem;
import fr.azodox.blocks.ICBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class CBlockUtil {

    private CBlockUtil(){}

    public static int getLevel(ItemStack item){
        if(!item.hasItemMeta()) throw new IllegalArgumentException("The item must have a display name.");
        List<String> lore = item.getItemMeta().getLore();

        String line = lore.stream().filter(l -> ChatColor.stripColor(l).contains("Niveau")).findFirst().orElse(null);
        String level = ChatColor.stripColor(line).substring(9, 12);

        if(level.charAt(0) == 'I' && level.charAt(1) == ' ') return 1;
        if(level.charAt(0) == 'I' && level.charAt(1) == 'I' && level.charAt(2) == ' ') return 2;
        if(level.charAt(0) == 'I' && level.charAt(1) == 'I' && level.charAt(2) == 'I') return 3;
        return 0;
    }

    /**
     * Check if a placed block is a claim block
     * @param block : the placed blcok
     * @return is a claim block
     */
    public static boolean isAClaimBlock(Block block){
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
                return false;
            }

            if(region.getId().contains("claim")){
                return true;
            }
        }
        return true;
    }

    /**
     * Get level on a placed (potentially) claim block
     * @return claim block level or 0 if the block isn't a claim block
     */
    public static int getClaimBlockLevel(Block block){
        if(!isAClaimBlock(block)){
            return 0;
        }

        Location location = block.getLocation();
        ProtectedRegion region = WGRegionUtil.getRegionAt(location);
        int xSize = region.getMaximumPoint().getBlockX() - region.getMinimumPoint().getBlockX();
        int zSize = region.getMaximumPoint().getBlockZ() - region.getMinimumPoint().getBlockZ();

        ClaimSystem claimSystem = (ClaimSystem) Bukkit.getPluginManager().getPlugin("ClaimSystem");
        for (ICBlock cBlock : claimSystem.getRegisteredCBlocks()) {
            if(cBlock.getXSize() == xSize && cBlock.getZSize() == zSize){
                return Integer.parseInt(cBlock.getLevel(false));
            }
        }
        return 0;
    }

    public static ICBlock getClaimBlockFromLevel(int level){
        ClaimSystem claimSystem = (ClaimSystem) Bukkit.getPluginManager().getPlugin("ClaimSystem");
        return claimSystem.getRegisteredCBlocks().stream().filter(c -> c.getLevel(false).equals(String.valueOf(level))).findFirst().orElse(null);
    }

    /*public static void updateCuboid(Cuboid from, Cuboid to){
        List<Block> blocksToUpdate = new ArrayList<>();
        from.getBlocks().forEach(b -> {
            if(!to.containsBlock(b)){
                blocksToUpdate.add(b);
            }
        });

        blocksToUpdate.forEach(b -> b.getState().update());
    }*/
}
