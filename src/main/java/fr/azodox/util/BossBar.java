package fr.azodox.util;

import fr.azodox.ClaimSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class BossBar {

    private static final ClaimSystem main = (ClaimSystem) Bukkit.getPluginManager().getPlugin("ClaimSystem");
    private static final Map<UUID, org.bukkit.boss.BossBar> bossBars = new HashMap<>();

    static {
        initBossBarsChecker();
    }

    public static void sendBossBar(Player player, org.bukkit.boss.BossBar bossBar){
        sendBossBar(player.getUniqueId(), bossBar);
    }

    public static void sendBossBar(UUID playerUUID, org.bukkit.boss.BossBar bossBar){

        if(bossBars.containsKey(playerUUID)){
            org.bukkit.boss.BossBar bsb = bossBars.get(playerUUID);
            if(bsb.getTitle().equals(bossBar.getTitle())) return;
            bsb.setVisible(false);
            bsb.removePlayer(Objects.requireNonNull(Bukkit.getPlayer(playerUUID)));
            bossBars.remove(playerUUID);
        }

        bossBars.put(playerUUID, bossBar);
        bossBar.setVisible(true);
        bossBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(playerUUID)));
    }

    public static void removeBossBar(UUID playerUUID){
        org.bukkit.boss.BossBar bsb = bossBars.get(playerUUID);
        bsb.removePlayer(Objects.requireNonNull(Bukkit.getPlayer(playerUUID)));
        bossBars.remove(playerUUID);
    }

    public static org.bukkit.boss.BossBar getBossBar(Player player){
        return getBossBar(player.getUniqueId());
    }

    public static org.bukkit.boss.BossBar getBossBar(UUID playerUUID){
        return bossBars.get(playerUUID);
    }

    private static void initBossBarsChecker(){
        Bukkit.getScheduler().runTaskTimer(main, () -> bossBars.forEach((u, b) -> {
            if(Bukkit.getPlayer(u) != null){
                Player player = Bukkit.getPlayer(u);
                ItemStack item = player.getInventory().getItemInMainHand();
                if(!(item.hasItemMeta() && item.getItemMeta().getDisplayName().contains("§e§lBloc de claim"))){
                    b.removePlayer(player);
                }else{
                    if(b.getProgress() >= 0.99){
                        b.setProgress(0.01);
                        return;
                    }
                    b.setProgress(b.getProgress() + 0.01);
                }
            }
        }), 0, 1);
    }

}