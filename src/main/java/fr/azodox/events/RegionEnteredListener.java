package fr.azodox.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public class RegionEnteredListener implements Listener {

    @EventHandler
    public void onRegionEntered(RegionEnteredEvent e){
        ProtectedRegion region = e.getRegion();
        if(region.getId().contains("claim")){
            Set<String> players = new HashSet<>();

            region.getOwners().getUniqueIds().forEach(uuid -> players.add(Bukkit.getOfflinePlayer(uuid).getName()));

            String owners = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + String.join("§f, §b", players) + ChatColor.DARK_AQUA + "]";
            e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW +
                    "Entrer" + ChatColor.DARK_GRAY + ") " + ChatColor.GRAY + "Zone de " + owners));
        }
    }
}
