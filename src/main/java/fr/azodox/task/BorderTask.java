package fr.azodox.task;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import fr.azodox.ClaimSystem;

public class BorderTask extends BukkitRunnable {

  private final ClaimSystem claimSystem;
  private final Map<Long, List<Location>> borders;

  public BorderTask(ClaimSystem claimSystem) {
    this.claimSystem = claimSystem;
    this.borders = claimSystem.getBordersParticles();
  }

  @Override
  public void run() {
    if(borders != null && !borders.isEmpty()){
      for(List<Location> locations : borders.values()){
        for(Location location : locations){
          
        }
      }
    }
  }
}
