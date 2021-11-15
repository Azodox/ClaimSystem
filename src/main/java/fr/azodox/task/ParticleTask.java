package fr.azodox.task;

import fr.azodox.ClaimSystem;
import fr.azodox.particle.Particle;
import fr.azodox.particle.ParticleData;
import fr.azodox.particle.ParticleType;
import fr.azodox.util.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public final class ParticleTask extends BukkitRunnable {

    private final ClaimSystem claimSystem;

    public ParticleTask(ClaimSystem claimSystem) {
        this.claimSystem = claimSystem;
    }

    @Override
    public void run() {
        if (claimSystem.getFakesParticles().isEmpty()) {
            return;
        }

        for (UUID uuid : claimSystem.getFakesParticles().keySet()) {
            Cuboid cuboid = claimSystem.getFakesParticles().get(uuid);
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                for (Location l : cuboid.getBounds()) {
                    new Particle(player, ParticleType.of("redstone"),
                            ParticleData.createDustOptions(Color.RED, 3))
                            .getType().spawn(player, l, 1);
                }
            }
        }
    }
}
