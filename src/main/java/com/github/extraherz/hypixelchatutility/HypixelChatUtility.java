package com.github.extraherz.hypixelchatutility;

import com.github.extraherz.hypixelchatutility.gui.GuiChat;
import com.github.extraherz.hypixelchatutility.handler.ChatHandler;
import com.github.extraherz.hypixelchatutility.handler.KeybindHandler;
import com.github.extraherz.hypixelchatutility.manager.ChatManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = HypixelChatUtility.MODID, version = HypixelChatUtility.VERSION, name = HypixelChatUtility.NAME, useMetadata = true)
public class HypixelChatUtility {
    public static final String MODID = "hypixelchatutility";
    public static final String VERSION = "1.0.0+4-DEV";
    public static final String NAME = "Hypixel Chat Utility";

    @Mod.Instance(MODID)
    public static HypixelChatUtility instance;

    private ChatManager chatManager;
    private GuiChat guildChatGui;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.chatManager = new ChatManager();
        this.guildChatGui = new GuiChat(chatManager);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ChatHandler(chatManager));
        MinecraftForge.EVENT_BUS.register(new KeybindHandler());
        MinecraftForge.EVENT_BUS.register(guildChatGui);
    }

    public static HypixelChatUtility getInstance() {
        return instance;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public GuiChat getGuildChatGui() {
        return guildChatGui;
    }
}
