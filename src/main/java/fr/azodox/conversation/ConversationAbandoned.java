package fr.azodox.conversation;

import fr.azodox.ClaimSystem;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.jetbrains.annotations.NotNull;

public class ConversationAbandoned implements ConversationAbandonedListener {

    @Override
    public void conversationAbandoned(@NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
        if (!conversationAbandonedEvent.gracefulExit()) {
            conversationAbandonedEvent.getContext().getForWhom().sendRawMessage(ClaimSystem.PLUGIN_PREFIX + "Conversation automatiquement terminée.");
        }
    }
}
