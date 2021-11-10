package fr.azodox.blocks;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.azodox.ClaimSystem;
import fr.azodox.conversation.ClaimConversationPrefix;
import fr.azodox.conversation.ConversationAbandoned;
import fr.azodox.conversation.WhichPlayerPrompt;
import fr.azodox.inventory.CBlockInventory;
import fr.azodox.inventory.util.FastInv;
import fr.azodox.util.HeadUtil;
import fr.azodox.util.ItemBuilder;
import fr.azodox.util.WGRegionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CBlockLI implements ICBlock, CBlockInventory {

    @Override
    public String getName() {
        return ChatColor.YELLOW + "§lBloc de claim §6(I)";
    }

    @Override
    public ItemStack getItem(String player) {
        return new ItemBuilder(Material.GOLD_BLOCK)
                .setAmount(1)
                .setName(getName())
                .addEnchantment(Enchantment.LOYALTY, 1)
                .setLore(
                        "§8§m                        ",
                        "§8§l| §fAide",
                        "",
                        "§7Voici un bloc de claim, il permet de créer un claim.",
                        "§7Pour l'utiliser, il suffit de le prendre en main.",
                        "§7Ensuite, pour créer un claim, posez le bloc.",
                        "§7Il sera instantanément créer et ce bloc en sera le centre.",
                        "",
                        "§8§l| §fInformations",
                        "",
                        "§2§l➲ Niveau I §7= §f50x50",
                        "",
                        "§9Appartient à §l" + player,
                        "§8§m                        ",
                        "§bBesoin d'aide ? §8§l➠ §bContactez un §lGuide"
                )
                .setUnbreakable(true)
                .withItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES,  ItemFlag.HIDE_UNBREAKABLE)
                .build();
    }

    @Override
    public String getLevel(boolean letter) {
        return letter ? "I" : "1";
    }

    @Override
    public double getXSize() {
        return 50d;
    }

    @Override
    public double getZSize() {
        return 50d;
    }

    @Override
    public CBlockInventory getInventory() {
        return this;
    }

    @Override
    public void open(Player player, ProtectedRegion region) {
        get(region).open(player);
    }
}
