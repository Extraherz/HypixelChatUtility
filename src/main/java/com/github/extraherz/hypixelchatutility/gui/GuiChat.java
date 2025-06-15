package com.github.extraherz.hypixelchatutility.gui;

import com.github.extraherz.hypixelchatutility.manager.ChatManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiChat {

    private static final int CHAT_WIDTH = 320;
    private static final int CHAT_HEIGHT = 180;
    private static final int LINE_HEIGHT = 9;
    private static final int MARGIN = 2;

    private final Minecraft mc;
    private final ChatManager chatManager;

    public GuiChat(ChatManager chatManager) {
        this.mc = Minecraft.getMinecraft();
        this.chatManager = chatManager;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.CHAT) {
            return;
        }

        if (mc.gameSettings.chatVisibility == EntityPlayer.EnumChatVisibility.HIDDEN) { // Hidden
            return;
        }

        renderGuildChat();
    }

    private void renderGuildChat() {
        ScaledResolution scaledRes = new ScaledResolution(mc);
        List<ChatManager.ChatMessage> messages = chatManager.getVisibleMessages();

        if (messages.isEmpty()) {
            return;
        }

        GlStateManager.pushMatrix();

        // Apply chat scale first
        float chatScale = mc.gameSettings.chatScale;
        GlStateManager.scale(chatScale, chatScale, 1.0f);

        // Position on the right side of the screen (after scaling)
        int scaledWidth = (int)(scaledRes.getScaledWidth() / chatScale);
        int scaledHeight = (int)(scaledRes.getScaledHeight() / chatScale);

        int chatX = scaledWidth - CHAT_WIDTH - 2;
        int chatY = scaledHeight - 40; // Same height as normal chat

        FontRenderer fontRenderer = mc.fontRendererObj;
        boolean chatOpen = chatManager.isChatOpen();

        // Calculate total height needed
        int totalMessages = messages.size();
        int maxLines = chatOpen ? 20 : 10;
        int linesToShow = Math.min(totalMessages, maxLines);

        // Start from bottom and work up (like Minecraft chat)
        int currentY = chatY;

        // Draw messages from newest to oldest (bottom to top)
        for (int i = 0; i < linesToShow; i++) {
            ChatManager.ChatMessage chatMessage = messages.get(i);
            IChatComponent message = chatMessage.getMessage();

            // Calculate alpha based on fade
            float alpha = chatMessage.getFadeAlpha();
            if (alpha <= 0.0f) {
                continue;
            }

            // Apply chat opacity setting when chat is closed
            if (!chatOpen) {
                alpha *= mc.gameSettings.chatOpacity;
            }

            if (alpha > 0.03f) {
                String messageText = message.getFormattedText();

                // Calculate position for this line
                int lineY = currentY - (i + 1) * LINE_HEIGHT;

                // Draw background rectangle like Minecraft chat
                if (!chatOpen) {
                    //int textWidth = fontRenderer.getStringWidth(messageText);
                    int backgroundColor = (int)(alpha * 127.0f) << 24;
                    Gui.drawRect(chatX - 2, lineY - 1, chatX + CHAT_WIDTH + 2, lineY + LINE_HEIGHT - 1, backgroundColor);
                }

                // Set text color and alpha
                GlStateManager.enableBlend();

                // Draw the message text
                fontRenderer.drawStringWithShadow(messageText, chatX, lineY,
                        (int)(alpha * 255.0f) << 24 | 0xFFFFFF);

                GlStateManager.disableBlend();
            }
        }

        // Draw chat background when open (like Minecraft)
        if (chatOpen && linesToShow > 0) {
            int bgHeight = linesToShow * LINE_HEIGHT + 4;
            int bgY = currentY - bgHeight;
            // Semi-transparent black background like Minecraft chat
            Gui.drawRect(chatX - 2, bgY, chatX + CHAT_WIDTH, currentY, Integer.MIN_VALUE);
        }

        // Draw scroll indicator if needed
        if (chatOpen && chatManager.getScrollOffset() > 0) {
            String scrollText = "↑ " + chatManager.getScrollOffset() + " more lines";
            int scrollY = currentY - (linesToShow + 1) * LINE_HEIGHT;
            fontRenderer.drawStringWithShadow(scrollText, chatX, scrollY, 0x808080);
        }

        GlStateManager.popMatrix();
    }

    public void updateChatOpen(boolean open) {
        chatManager.setChatOpen(open);
    }

    public void scrollChat(int direction) {
        chatManager.scroll(direction);
    }

}
