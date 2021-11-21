package fr.azodox.task;

import fr.azodox.ClaimSystem;
import fr.azodox.particle.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;

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
          System.out.println("particle location : " + particle.getLocation());
        }
      }
    }
  }
}
