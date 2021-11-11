package fr.azodox.inventory;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.azodox.ClaimSystem;
import fr.azodox.conversation.ClaimConversationPrefix;
import fr.azodox.conversation.ConversationAbandoned;
import fr.azodox.conversation.WhichPlayerPrompt;
import fr.azodox.inventory.permissions.EditPermissionsInventory;
import fr.azodox.inventory.util.FastInv;
import fr.azodox.util.HeadUtil;
import fr.azodox.util.ItemBuilder;
import fr.azodox.util.WGRegionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class CBlockInventory extends FastInv {

    public CBlockInventory(String title, Player player, ProtectedRegion region) {
        super(5 * 9, title);

        for (int border : getBorders()) {
          setItem(border, new ItemBuilder(Material.YELLOW_STAINED_GLASS).setName(" ").build());
        }

        setItem(34, new ItemBuilder(Material.BARRIER)
        .setName(ChatColor.RED + "Supprimer le claim*")
        .setLore(
                "§8§m                        ",
                "§8§l| §fAide",
                "",
                ChatColor.GRAY + "Appuyer sur ce bouton supprimera votre claim.",
                ChatColor.GRAY + "Cette opération est irréversible.",
                ChatColor.GRAY + "Vous devrez recréer votre claim si vous souhaitez le récupérer.",
                "§8§m                        ",
                "",
                ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "TIP" + ChatColor.DARK_GRAY + "]" +
                        ChatColor.GRAY + " La présence de " + ChatColor.DARK_GRAY + "'" + ChatColor.YELLOW +
                        "*" + ChatColor.DARK_GRAY + "' " +
                        ChatColor.GRAY + "indique qu'un inventaire de confirmation ",
                ChatColor.GRAY + "est présent avant la finalisation de l'action.",
                ChatColor.GRAY + "Celui-ci devra être confirmer (ou non)."
        ).build(), e -> {new ConfirmInventory(this, region, ConfirmationReason.DELETE).open(player);});

        setItem(22, new ItemBuilder(Material.ENDER_PEARL)
        .setName(ChatColor.DARK_AQUA + "Afficher les limites")
        .setLore(
                "§8§m                        ",
                "§8§l| §fAide",
                "",
                ChatColor.GRAY + "Appuyer sur ce bouton vous permettra de voir les limites de votre claim.",
                ChatColor.GRAY + "Ceci vous permet de voir la taille de votre claim.",
                ChatColor.GRAY + "La taille que vous voyez est la taille totale (de la couche " + ChatColor.DARK_AQUA + "0" + ChatColor.GRAY + " à " + ChatColor.DARK_AQUA + "256" + ChatColor.GRAY + ").",
                "§8§m                        "
        )
        .build());
        
        setItem(33, new ItemBuilder(HeadUtil.getHead("plus"))
                .setName(ChatColor.AQUA + "Ajouter un joueur")
                .setLore(
                        "§8§m                        ",
                        "§8§l| §fAide",
                        "",
                        ChatColor.GRAY + "Ce bouton permet d'ajouter un joueur",
                        ChatColor.GRAY + "dans les personnes de confiance de votre claim.",
                        ChatColor.GRAY + "Une fois ajouté, celui-ci sera présent comme membre de votre claim.",
                        "§8§m                        ",
                        "",
                        ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "TIP" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Pour gérer les permissions des " + ChatColor.AQUA + "Membres," + ChatColor.GRAY + " utiliser le bouton",
                        ChatColor.GRAY + "dans le menu de votre claim (celui-ci) représenté par la clé à molette."
                )
                .build(), e -> {
            ClaimSystem claimSystem = (ClaimSystem) Bukkit.getPluginManager().getPlugin("ClaimSystem");

            player.closeInventory();
            assert claimSystem != null;
            ConversationFactory factory = new ConversationFactory(claimSystem)
                    .withLocalEcho(false)
                    .withTimeout(20)
                    .withFirstPrompt(new WhichPlayerPrompt(claimSystem))
                    .withPrefix(new ClaimConversationPrefix())
                    .withInitialSessionData(Map.of(
                            "location", WGRegionUtil.toBukkitLocation(Bukkit.getWorld("world"), region.getMinimumPoint()), 
                            "inventory", this, 
                            "whom", player,
                            "action", "add"))
                    .thatExcludesNonPlayersWithMessage("Pas toi.")
                    .addConversationAbandonedListener(new ConversationAbandoned());

            factory.buildConversation(player).begin();
        });

        setItem(32, new ItemBuilder(HeadUtil.getHead("minus"))
                .setName(ChatColor.RED + "Retirer un joueur")
                .setLore(
                        "§8§m                        ",
                        "§8§l| §fAide",
                        "",
                        ChatColor.GRAY + "Ce bouton permet de retirer un joueur",
                        ChatColor.GRAY + "parmi les personnes de confiance de votre claim.",
                        ChatColor.GRAY + "Pour voir les personnes dans votre claim, utilisez le papier.",
                        "§8§m                        "
                ).build(), e -> {
                        ClaimSystem claimSystem = (ClaimSystem) Bukkit.getPluginManager().getPlugin("ClaimSystem");

                        player.closeInventory();
                        assert claimSystem != null;
                        ConversationFactory factory = new ConversationFactory(claimSystem)
                                .withLocalEcho(false)
                                .withTimeout(20)
                                .withFirstPrompt(new WhichPlayerPrompt(claimSystem))
                                .withPrefix(new ClaimConversationPrefix())
                                .withInitialSessionData(Map.of(
                                        "location", WGRegionUtil.toBukkitLocation(Bukkit.getWorld("world"), region.getMinimumPoint()), 
                                        "inventory", this, 
                                        "whom", player,
                                        "action", "remove"))
                                .thatExcludesNonPlayersWithMessage("Pas toi.")
                                .addConversationAbandonedListener(new ConversationAbandoned());

                        factory.buildConversation(player).begin();
                });

        //TODO : Code this feature, i.e edit members' permissions.
        setItem(10, new ItemBuilder(HeadUtil.getHead("wrench"))
                .setName(ChatColor.YELLOW + "Editer les permissions des membres")
                .setLore(
                        new TreeSet<>(WGRegionUtil.getFlags(region).keySet())
                )
                .build(), e -> {
                        new EditPermissionsInventory(player, region).open(player);
                });

        List<String> members = new ArrayList<>();
        region.getMembers().getUniqueIds().forEach(u -> {
            if(!u.equals(player.getUniqueId())){
                members.add(ChatColor.DARK_GRAY + "• " + ChatColor.AQUA + Bukkit.getOfflinePlayer(u).getName());
            }
        });

        setItem(16, new ItemBuilder(Material.PAPER)
                .setName(ChatColor.GRAY + "Membres" + ChatColor.DARK_GRAY + " (" + ChatColor.GRAY + members.size() + ChatColor.DARK_GRAY + ")")
                .setLore(members)
                .build());
    }
}
