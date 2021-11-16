package fr.azodox.task;

import java.util.List;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;

import fr.azodox.ClaimSystem;
import fr.azodox.particle.Particle;

public class BorderTask extends BukkitRunnable {

  private final Map<Long, List<Particle>> borders;

  public BorderTask(ClaimSystem claimSystem) {
    this.borders = claimSystem.getBordersParticles();
  }

  @Override
  public void run() {
    if(borders != null && !borders.isEmpty()){
      for(List<Particle> particles : borders.values()){
        for(Particle particle : particles){
          particle.getType().spawn(particle.getPlayer(), particle.getLocation(), 1);
        }
      }
    }
  }
}
