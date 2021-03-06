package fr.azodox.conversation;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.azodox.ClaimSystem;
import fr.azodox.util.WGRegionUtil;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.PlayerNamePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WhichPlayerPrompt extends PlayerNamePrompt {

    public WhichPlayerPrompt(@NotNull Plugin plugin) {
        super(plugin);
    }

    @Nullable
    @Override
    protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Player player) {
        Player whom = (Player) conversationContext.getSessionData("whom");
        if (player.getUniqueId().equals(whom.getUniqueId())) {
            conversationContext.getForWhom().sendRawMessage(ClaimSystem.PLUGIN_PREFIX + "Vous ne pouvez pas vous " + (conversationContext.getSessionData("action").equals("add") ? "ajouter " : "retirer ") +"vous-même.");
            return Prompt.END_OF_CONVERSATION;
        }

        ProtectedRegion region = WGRegionUtil.getRegionAt((Location) conversationContext.getSessionData("location"));
        if (region.getMembers().contains(player.getUniqueId())) {
            conversationContext.getForWhom().sendRawMessage(ClaimSystem.PLUGIN_PREFIX + "Ce joueur est déjà dans votre claim.");
            return Prompt.END_OF_CONVERSATION;
        }

        conversationContext.setSessionData("player", player);
        return new ManagedPlayerMsgPrompt();
    }

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return "Quel joueur souhaitez-vous " + (conversationContext.getSessionData("action").equals("add") ? "ajouter " : "retirer ") + "?";
    }
}
