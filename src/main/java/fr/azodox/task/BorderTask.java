package fr.azodox.task;

import com.google.common.collect.Iterators;
import fr.azodox.ClaimSystem;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class BorderTask extends BukkitRunnable {

  private final Map<Long, Player> borders;
  private final Iterator<Entry<Long, Player>> iterator;

  public BorderTask(ClaimSystem claimSystem) {
      this.borders = claimSystem.getBorders();
      this.iterator = Iterators.cycle(this.borders.entrySet());
  }

  @Override
  public void run() {
      if(this.borders != null && !this.borders.isEmpty()){
        if (iterator.hasNext()){
          var entry = iterator.next();
          if((System.currentTimeMillis() / 1000) - (entry.getKey() / 1000) >= 10){
              iterator.remove();
              ClaimSystem.getBorderAPI().getBorderManager().getPlayerBorder(entry.getValue()).remove(entry.getValue());
          }
        }
      }
  }
}
