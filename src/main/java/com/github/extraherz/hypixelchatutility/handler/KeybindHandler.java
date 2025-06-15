package com.github.extraherz.hypixelchatutility.handler;

import com.github.extraherz.hypixelchatutility.HypixelChatUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class KeybindHandler {

    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean wasChatOpen = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        // Check if chat open state changed
        boolean isChatOpen = mc.currentScreen instanceof GuiChat;
        if (isChatOpen != wasChatOpen) {
            HypixelChatUtility.getInstance().getGuildChatGui().updateChatOpen(isChatOpen);
            wasChatOpen = isChatOpen;
        }
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (!Mouse.getEventButtonState()) {
            return;
        }

        // Handle mouse wheel scrolling in chat
        if (mc.currentScreen instanceof GuiChat) {
            int wheel = Mouse.getEventDWheel();
            if (wheel != 0) {
                // Scroll up = positive wheel, scroll down = negative wheel
                // We want positive wheel to show older messages (scroll up in history)
                int direction = wheel > 0 ? 1 : -1;
                HypixelChatUtility.getInstance().getGuildChatGui().scrollChat(direction);
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) {
            return;
        }

        // Handle keyboard scrolling in chat
        if (mc.currentScreen instanceof GuiChat) {
            int key = Keyboard.getEventKey();

            // Page Up = scroll up (show older messages)
            if (key == Keyboard.KEY_PRIOR) {
                HypixelChatUtility.getInstance().getGuildChatGui().scrollChat(5);
            }
            // Page Down = scroll down (show newer messages)
            else if (key == Keyboard.KEY_NEXT) {
                HypixelChatUtility.getInstance().getGuildChatGui().scrollChat(-5);
            }
            // Home = scroll to newest messages
            else if (key == Keyboard.KEY_HOME) {
                HypixelChatUtility.getInstance().getChatManager().scroll(-1000); // Scroll to bottom
            }
            // End = scroll to oldest messages
            else if (key == Keyboard.KEY_END) {
                HypixelChatUtility.getInstance().getChatManager().scroll(1000); // Scroll to top
            }
        }
    }

}
