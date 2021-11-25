package fr.azodox.task;

import com.google.common.collect.Iterators;
import fr.azodox.ClaimSystem;
import fr.azodox.util.BorderUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class BorderTask extends BukkitRunnable {

  private final Map<Long, Player> borders;

  public BorderTask(ClaimSystem claimSystem) {
      this.borders = claimSystem.getBorders();
  }

  @Override
  public void run() {
      if(this.borders != null && !this.borders.isEmpty()){
          var iterator = Iterators.cycle(this.borders.entrySet());

          while (iterator.hasNext()){
              var entry = iterator.next();
              if((System.currentTimeMillis() / 1000) - (entry.getKey() / 1000) >= 10){
                  iterator.remove();
                  BorderUtil.resetBorder(entry.getValue());
              }
          }
      }
  }
}
