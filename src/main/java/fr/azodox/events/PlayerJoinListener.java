package fr.azodox.events;

import fr.azodox.ClaimSystem;
import fr.azodox.particle.Particle;
import fr.azodox.particle.ParticleData;
import fr.azodox.particle.ParticleType;
import fr.azodox.particle.Rotator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

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

        new BukkitRunnable(){

            private final Location LOCATION = player.getLocation();
            private int i = 0;

            @Override
            public void run() {
                i+=10;
                for (int degree = 0; degree < 360; degree++) {
                    var random = new Random();

                    double radians = Math.toRadians(degree);
                    double x = 6 * Math.cos(radians);
                    double z = 6 * Math.sin(radians);

                    Vector zVector = Rotator.rotateAroundAxisZ(new Vector(x, 0, z), i);
                    Vector yVector = Rotator.rotateAroundAxisY(new Vector(x, 0, z), i);
                    Vector negaYVector = Rotator.rotateAroundAxisY(new Vector(x, 0, -z), i);
                    Vector negaZVector = Rotator.rotateAroundAxisY(new Vector(x, 0, -z), i);

                    Location location = LOCATION.clone();
                    location.add(zVector);
                    location.add(yVector);
                    location.add(negaYVector);
                    location.add(negaZVector);

                    var particle = new Particle(player, location, ParticleType.of(player, "redstone"), null);
                    var particleType = particle.getType();
                    var randomChatColor = ChatColor.of(String.format("#%06x", random.nextInt(0xffffff + 1)));
                    particleType.spawn(player, location, 1, ParticleData.createDustOptions(Color.fromRGB(randomChatColor.getColor().getRGB()), 2));

                    location.subtract(zVector);
                    location.subtract(yVector);
                    location.subtract(negaYVector);
                    location.subtract(negaZVector);
                }
            }
        }.runTaskTimer(this.claimSystem, 0, 1);
    }
}
