package com.github.extraherz.hypixelchatutility.handler;

import com.github.extraherz.hypixelchatutility.manager.ChatManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ChatHandler {

    private final ChatManager chatManager;

    public ChatHandler(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        // Only process normal chat messages (type 0), not system messages
        if (event.type != 0) {
            return;
        }

        // Check if it's a guild message
        String messageText = event.message.getUnformattedText();
        if (messageText.startsWith("Guild >")) {
            // Add to guild chat
            chatManager.addMessage(event.message);
            // Cancel the event, so it doesn't appear in normal chat
            event.setCanceled(true);
        }
    }

}
