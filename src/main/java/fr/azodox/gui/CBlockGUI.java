package fr.azodox.gui;

import fr.azodox.ClaimSystem;
import fr.azodox.blocks.ICBlock;
import fr.azodox.util.ActionBar;
import fr.azodox.util.BossBar;
import fr.azodox.util.Cuboid;
import fr.azodox.util.WGRegionUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class CBlockGUI {

    private final Player player;
    private final ClaimSystem main;

    public CBlockGUI(Player player, ClaimSystem main) {
        this.player = player;
        this.main = main;
    }

    public static void removeGUI(Player player){
        ActionBar.removeMessage(player, true);
        if(BossBar.getBossBar(player.getUniqueId()) != null) {
            BossBar.removeBossBar(player.getUniqueId());
        }

        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null &&
                player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().contains("§6§lLégende")) {
            if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
        }
    }

    public void show(ICBlock cBlock, boolean moveEvent){
        Block tB = player.getTargetBlock(null, 10);
        Location loc = tB.getLocation();

        if(loc.getY() != loc.getWorld().getHighestBlockAt(loc).getY()){
            return;
        }

        Cuboid cuboid = new Cuboid(loc, loc.clone().add(cBlock.getXSize(), 1, 0), loc.clone().add(0, 1, cBlock.getZSize()));

        if(moveEvent){
            if(main.getFakesParticles().containsKey(player.getUniqueId())){
                main.getFakesParticles().remove(player.getUniqueId(), cuboid);
            }
        }

        boolean isValid = WGRegionUtil.isValid(player, cuboid);

        if(WGRegionUtil.getRegionsNumber(player.getLocation().getWorld(), player) >= ClaimSystem.MAX_CLAIMS){
            org.bukkit.boss.BossBar bossBar = Bukkit.createBossBar(
                    "§4⚠ " + ChatColor.RED + "Erreur : Vous avez atteint le nombre maximum de claim" + " §4⚠",
                    BarColor.RED,
                    BarStyle.SEGMENTED_6,
                    BarFlag.CREATE_FOG
            );

            bossBar.setProgress(0.0);
            BossBar.sendBossBar(player, bossBar);
            isValid = false;
        }

        if(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null || !player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().contains("§6§lLégende")){
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective help = sb.registerNewObjective("help-" + player.getName().toLowerCase(), "dummy", "§6§lLégende");
            help.setDisplaySlot(DisplaySlot.SIDEBAR);
            help.getScore("§e").setScore(3);
            help.getScore("§2§oVert §8§l➥ §fClaim possible ✓").setScore(2);
            help.getScore("§4§oRouge §8§l➥ §fClaim impossible §c❌").setScore(1);
            //help.getScore("§7§nNoir §8§l➥f §fTaille maximal d'un claim").setScore(1);
            help.getScore("§c").setScore(0);

            //player.setScoreboard(sb);
        }

        main.getFakesParticles().put(player.getUniqueId(), cuboid);

        if(isValid){
            ActionBar.removeMessage(player, true);
            ActionBar.sendPermanentMessage(player, new TextComponent("§6»§e»§6» §eClique droit pour confirmer §6«§e«§6«"));
        }else{
            ActionBar.removeMessage(player, true);
            ActionBar.sendPermanentMessage(player, new TextComponent(
                    ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "»»» Clique droit pour confirmer «««"));
        }
    }
}
