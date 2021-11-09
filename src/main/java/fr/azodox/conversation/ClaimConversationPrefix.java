package fr.azodox.conversation;

import fr.azodox.ClaimSystem;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.jetbrains.annotations.NotNull;

public class ClaimConversationPrefix implements ConversationPrefix {
    @NotNull
    @Override
    public String getPrefix(@NotNull ConversationContext conversationContext) {
        return ClaimSystem.PLUGIN_PREFIX;
    }
}
