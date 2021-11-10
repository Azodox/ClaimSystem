package fr.azodox.inventory;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;

public abstract class CBlockInventory {

    protected String name;

    protected CBlockInventory(String name){
        this.name = name;
    }

    abstract void open(Player player, ProtectedRegion region);

    protected Inventory get(ProtectedRegion region){
        FastInv inv = new FastInv(5 * 9, this.name);
        for (int border : inv.getBorders()) {
            inv.setItem(border, new ItemBuilder(Material.YELLOW_STAINED_GLASS).setName(" ").build());
        }

        inv.setItem(34, new ItemBuilder(Material.BARRIER)
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
                ).build());

        inv.setItem(22, new ItemBuilder(Material.ENDER_PEARL)
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

        /*TODO : Add item to add player in the claim
         *  Add item to see players added in the claim
         *  Complete all lorries.
         */

        inv.setItem(33, new ItemBuilder(HeadUtil.getHead("plus"))
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
                    .withInitialSessionData(Map.of("location", WGRegionUtil.toBukkitLocation(Bukkit.getWorld("world"), region.getMinimumPoint()), "inventory", getInventory(), "whom", player))
                    .thatExcludesNonPlayersWithMessage("Pas toi.")
                    .addConversationAbandonedListener(new ConversationAbandoned());

            factory.buildConversation(player).begin();
        });

        inv.setItem(10, new ItemBuilder(HeadUtil.getHead("wrench")).setName(ChatColor.YELLOW + "Editer les permissions des membres").build());

        List<String> members = new ArrayList<>();
        region.getMembers().getUniqueIds().forEach(u -> {
            if(!u.equals(player.getUniqueId())){
                members.add(ChatColor.DARK_GRAY + "• " + ChatColor.AQUA + Bukkit.getOfflinePlayer(u).getName());
            }
        });

        inv.setItem(16, new ItemBuilder(Material.PAPER)
                .setName(ChatColor.GRAY + "Membres" + ChatColor.DARK_GRAY + " (" + ChatColor.GRAY + members.size() + ChatColor.DARK_GRAY + ")")
                .setLore(members)
                .build());
    }
}
