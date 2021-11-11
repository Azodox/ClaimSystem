package fr.azodox.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.azodox.ClaimSystem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class WGRegionUtil {

    private WGRegionUtil(){}

    public static int getRegionsNumber(World world, OfflinePlayer player){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));
        if(regions == null) return 0;

        return regions.getRegionCountOfPlayer(WorldGuardPlugin.inst().wrapOfflinePlayer(player));
    }

    public static boolean isInARegion(Cuboid cuboid){
        cuboid.getPoint1().setY(0);
        cuboid.getPoint2().setY(256);
        for (Block block : cuboid.getBlocks()) {
            if(isInARegion(block.getLocation())){
                return true;
            }
        }
        return false;
    }

    public static boolean isInARegion(Player player){
        return isInARegion(player.getLocation());
    }

    public static boolean isInARegion(Location location){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(Objects.requireNonNull(location.getWorld())));
        if(regions == null) return false;

        ApplicableRegionSet posRegions = regions.getApplicableRegions(BukkitAdapter.asBlockVector(location));
        return posRegions.size() != 0;
    }

    public static ProtectedRegion getRegionAt(Location location){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(Objects.requireNonNull(location.getWorld())));
        if(regions == null) return null;

        ApplicableRegionSet posRegions = regions.getApplicableRegions(BukkitAdapter.asBlockVector(location));
        if (posRegions.size() != 0) {
            return posRegions.getRegions().stream().findFirst().orElse(null);
        }
        return null;
    }

    public static int getRegionIndex(String regionId){
        return Integer.parseInt(regionId.substring(regionId.length() - 1));
    }

    public static List<String> getFlags(ProtectedRegion region){
        List<String> strings = new ArrayList<>();
        region.getFlags().forEach((f, v) -> {
            if(f instanceof StringFlag) {
                return;
            }
            strings.add(ChatColor.DARK_GRAY + "§l❯ " + ChatColor.WHITE + f.getName() + " " + ChatColor.AQUA + v.toString());
        });
        return strings;
    }

    public static void whenCreate(Player player){
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10f, 10f);
        player.spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 20);
        player.sendMessage(ClaimSystem.PLUGIN_PREFIX + "Bravo ! Vous venez de terminer la création de votre claim. " +
                ChatColor.GRAY + "[" + ChatColor.GREEN + WGRegionUtil.getRegionsNumber(player.getLocation().getWorld(), player) +
                ChatColor.DARK_GRAY + "/" + ChatColor.DARK_GREEN + ClaimSystem.MAX_CLAIMS + ChatColor.GRAY + "]");
    }

    public static Location getPointsCenter(Location point1, Location point2){
        return new Cuboid(null, point1, point2).getCenter();
    }
    public static Location getRegionCenter(ProtectedRegion region, World world){
        return new Cuboid(null, toBukkitLocation(world, region.getMinimumPoint()), toBukkitLocation(world, region.getMaximumPoint())).getCenter();
    }

    public static Location toBukkitLocation(World world, BlockVector3 vector){
        return new Location(world, vector.getX(), vector.getY(), vector.getZ());
    }

    public static boolean isValid(Player player, Cuboid cuboid){
        boolean isValid = WGRegionUtil.getRegionsNumber(player.getLocation().getWorld(), player) < ClaimSystem.MAX_CLAIMS;

        if(WGRegionUtil.isInARegion(cuboid)){
            isValid = false;
        }
        return isValid;
    }

    public static ProtectedRegion setFlagsForClaim(ProtectedRegion claim){
        claim.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
        claim.setFlag(Flags.BLOCK_BREAK.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
        claim.setFlag(Flags.BLOCK_PLACE, StateFlag.State.ALLOW);
        claim.setFlag(Flags.BLOCK_PLACE.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
        claim.setFlag(Flags.INTERACT, StateFlag.State.ALLOW);
        claim.setFlag(Flags.INTERACT.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
        claim.setFlag(Flags.USE, StateFlag.State.ALLOW);
        claim.setFlag(Flags.USE.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
        claim.setFlag(Flags.CHEST_ACCESS, StateFlag.State.ALLOW);
        claim.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
        claim.setFlag(Flags.ITEM_PICKUP, StateFlag.State.ALLOW);
        claim.setFlag(Flags.ITEM_PICKUP.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
        claim.setFlag(Flags.ITEM_DROP, StateFlag.State.ALLOW);
        claim.setFlag(Flags.ITEM_DROP.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
        claim.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
        claim.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);
        claim.setFlag(Flags.CREEPER_EXPLOSION, StateFlag.State.DENY);
        claim.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
        claim.setFlag(Flags.DAMAGE_ANIMALS, StateFlag.State.DENY);
        claim.setFlag(Flags.ENDERDRAGON_BLOCK_DAMAGE, StateFlag.State.DENY);
        claim.setFlag(Flags.PVP, StateFlag.State.DENY);
        claim.setFlag(Flags.CHEST_ACCESS, StateFlag.State.DENY);
        claim.setFlag(Flags.DENY_MESSAGE, ClaimSystem.PLUGIN_PREFIX + "Erreur : La region est protégée, ceci n'est pas possible ici.");
        claim.setFlag(Flags.DESTROY_VEHICLE, StateFlag.State.DENY);
        claim.setFlag(Flags.FIREWORK_DAMAGE, StateFlag.State.DENY);
        claim.setFlag(Flags.MOB_DAMAGE, StateFlag.State.DENY);
        claim.setFlag(Flags.WITHER_DAMAGE, StateFlag.State.DENY);
        claim.setFlag(Flags.INTERACT, StateFlag.State.DENY);
        claim.setFlag(Flags.USE, StateFlag.State.DENY);
        claim.setFlag(Flags.SNOW_MELT, StateFlag.State.DENY);
        claim.setFlag(Flags.ICE_MELT, StateFlag.State.DENY);
        claim.setFlag(Flags.ICE_FORM, StateFlag.State.DENY);
        claim.setFlag(Flags.FROSTED_ICE_FORM, StateFlag.State.DENY);

        return claim;
    }
}
