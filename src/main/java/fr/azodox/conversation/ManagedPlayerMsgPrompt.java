package fr.azodox.conversation;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.azodox.inventory.CBlockInventory;
import fr.azodox.util.WGRegionUtil;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManagedPlayerMsgPrompt extends MessagePrompt {

    @Nullable
    @Override
    protected Prompt getNextPrompt(@NotNull ConversationContext conversationContext) {
        return Prompt.END_OF_CONVERSATION;
    }

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        Player player = (Player) conversationContext.getSessionData("player");
        Player whom = (Player) conversationContext.getSessionData("whom");
        ProtectedRegion region = WGRegionUtil.getRegionAt((Location) conversationContext.getSessionData("location"));
        CBlockInventory inventory = (CBlockInventory) conversationContext.getSessionData("inventory");
        String action = (String) conversationContext.getSessionData("action");
        
        switch(action){
            case "add":
                region.getMembers().addPlayer(player.getUniqueId());
            break;

            case "remove":
                region.getMembers().removePlayer(player.getUniqueId());
            break;

            default: break;
        }

        inventory.open(whom);
        return "Joueur " + (action.equals("add") ? "ajouté" : "retiré") + " !";
    }
}
