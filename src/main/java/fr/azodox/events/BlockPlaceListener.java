package fr.azodox.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.azodox.ClaimSystem;
import fr.azodox.gui.CBlockGUI;
import fr.azodox.particle.Particle;
import fr.azodox.particle.ParticleData;
import fr.azodox.particle.ParticleType;
import fr.azodox.particle.Rotator;
import fr.azodox.util.Cuboid;
import fr.azodox.util.WGRegionUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BlockPlaceListener implements Listener {

    private final ClaimSystem claimSystem;

    public BlockPlaceListener(ClaimSystem claimSystem) {
        this.claimSystem = claimSystem;
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e){
        ItemStack item = e.getItemInHand(); /*represents the placed block in the player's hand*/
        Block block = e.getBlockPlaced();
        Player player = e.getPlayer();

        if(!item.hasItemMeta()){
            return;
        }

        if(!item.getItemMeta().hasDisplayName()){
            return;
        }

        if(item.getItemMeta().getDisplayName().contains("§e§lBloc de claim")){
            if(WGRegionUtil.isInARegion(block.getLocation()) || WGRegionUtil.getRegionsNumber(player.getLocation().getWorld(), player) >= ClaimSystem.MAX_CLAIMS){
                e.setCancelled(true);
                return;
            }

            /*int level = CBlockUtil.getLevel(item);

            if(level != 1){
                player.sendMessage(ClaimSystem.PLUGIN_PREFIX + ChatColor.RED + "Vous ne pouvez créer un claim qu'avec un claim bloc de §lLevel 1.");
                e.setCancelled(true);
                return;
            }*/

            Cuboid cuboid = claimSystem.getFakesParticles().get(player.getUniqueId());
            claimSystem.getFakesParticles().remove(player.getUniqueId());

            CBlockGUI.removeGUI(player);

            Location firstPoint = cuboid.getPoint1();
            Location secondPoint = cuboid.getPoint2();
            firstPoint.setY(0);
            secondPoint.setY(256);

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            Location center = cuboid.getCenter();
            RegionManager regions = container.get(BukkitAdapter.adapt(center.getWorld()));

            ProtectedCuboidRegion claim = new ProtectedCuboidRegion(
                    "claim_" +
                            player.getUniqueId() + "_"
                    + (WGRegionUtil.getRegionsNumber(player.getLocation().getWorld(), player) + 1),
                    BukkitAdapter.asBlockVector(firstPoint),
                    BukkitAdapter.asBlockVector(secondPoint)
            );

            claim.getOwners().addPlayer(player.getUniqueId());
            claim.getMembers().addPlayer(player.getUniqueId());

            if (regions != null) {
                regions.addRegion(WGRegionUtil.setFlagsForClaim(claim));
            }

            Block at = center.getWorld().getBlockAt(center);
            at.setType(Material.GOLD_BLOCK);

            WGRegionUtil.whenCreate(player);

            new BukkitRunnable(){

                private int i = 0;

                @Override
                public void run() {
                    i+=10;
                    for (int degree = 0; degree < 360; degree++) {
                        double radians = Math.toRadians(degree);
                        double x = 6 * Math.cos(radians);
                        double z = 6 * Math.sin(radians);

                        Vector vector = Rotator.rotateAroundAxisX(new Vector(x, 0, z), i);

                        Location location = at.getLocation();
                        location.add(vector);

                        var particle = new Particle(player, location, ParticleType.of(player, "redstone"), null);
                        var particleType = particle.getType();
                        particleType.spawn(player, location, 1, ParticleData.createDustOptions(Color.AQUA, 2));

                        location.subtract(vector);
                    }
                }
            }.runTaskTimer(this.claimSystem, 0, 1);

            e.setCancelled(true);
            player.getInventory().remove(player.getInventory().getItemInMainHand());
        }
    }
}
