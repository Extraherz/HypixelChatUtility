package com.github.extraherz.hypixelchatutility.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private static final int MAX_STORED_MESSAGES = 100;
    private static final int MAX_VISIBLE_MESSAGES_CLOSED = 10;
    private static final int MAX_VISIBLE_MESSAGES_OPEN = 20;
    private static final long MESSAGE_FADE_TIME = 12000; // 12 seconds in milliseconds

    private final List<ChatMessage> storedMessages;
    private final List<ChatMessage> visibleMessages;
    private int scrollOffset = 0;
    private boolean chatOpen = false;

    public ChatManager() {
        this.storedMessages = new ArrayList<>();
        this.visibleMessages = new ArrayList<>();
    }

    public void addMessage(IChatComponent message) {
        if (!isGuildMessage(message)) {
            return;
        }

        ChatMessage guildMessage = new ChatMessage(message, System.currentTimeMillis());

        // Add to stored messages
        storedMessages.add(0, guildMessage); // Add at beginning for chronological order

        // Remove old messages if we exceed the limit
        if (storedMessages.size() > MAX_STORED_MESSAGES) {
            storedMessages.remove(storedMessages.size() - 1);
        }

        // Reset scroll when new message arrives
        scrollOffset = 0;

        updateVisibleMessages();
    }

    private boolean isGuildMessage(IChatComponent message) {
        String text = message.getUnformattedText();
        return text.startsWith("Guild >");
    }

    public void updateVisibleMessages() {
        visibleMessages.clear();

        int maxVisible = chatOpen ? MAX_VISIBLE_MESSAGES_OPEN : MAX_VISIBLE_MESSAGES_CLOSED;
        long currentTime = System.currentTimeMillis();

        // Get messages starting from the most recent, considering scroll offset
        int startIndex = scrollOffset;
        int added = 0;

        for (int i = startIndex; i < storedMessages.size() && added < maxVisible; i++) {
            ChatMessage msg = storedMessages.get(i);

            // If chat is closed, check fade time
            if (!chatOpen && (currentTime - msg.getTimestamp()) > MESSAGE_FADE_TIME) {
                continue;
            }

            visibleMessages.add(msg);
            added++;
        }
    }

    public void setChatOpen(boolean open) {
        this.chatOpen = open;
        if (!open) {
            scrollOffset = 0; // Reset scroll when closing chat
        }
        updateVisibleMessages();
    }

    public boolean isChatOpen() {
        return chatOpen;
    }

    public void scroll(int direction) {
        if (!chatOpen) return;

        int oldOffset = scrollOffset;
        scrollOffset += direction;

        // Clamp scroll offset
        scrollOffset = Math.max(0, scrollOffset);
        scrollOffset = Math.min(Math.max(0, storedMessages.size() - MAX_VISIBLE_MESSAGES_OPEN), scrollOffset);

        if (oldOffset != scrollOffset) {
            updateVisibleMessages();
        }
    }

    public List<ChatMessage> getVisibleMessages() {
        updateVisibleMessages(); // Update in case of fade
        return new ArrayList<>(visibleMessages);
    }

    public void clearMessages() {
        storedMessages.clear();
        visibleMessages.clear();
        scrollOffset = 0;
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public int getStoredMessageCount() {
        return storedMessages.size();
    }

    public static class ChatMessage {
        private final IChatComponent message;
        private final long timestamp;

        public ChatMessage(IChatComponent message, long timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }

        public IChatComponent getMessage() {
            return message;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public float getFadeAlpha() {
            long age = System.currentTimeMillis() - timestamp;

            // If chat is open, always show full opacity
            if (Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) {
                return 1.0f;
            }

            // If message is too old, don't show it
            if (age > MESSAGE_FADE_TIME) {
                return 0.0f;
            }

            // Start fading in the last 1 second
            if (age > MESSAGE_FADE_TIME - 1000) {
                return Math.max(0.0f, (MESSAGE_FADE_TIME - age) / 1000.0f);
            }

            return 1.0f;
        }
    }

}
