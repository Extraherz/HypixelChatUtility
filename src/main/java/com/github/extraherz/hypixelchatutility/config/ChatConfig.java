package com.github.extraherz.hypixelchatutility.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ChatConfig {
    private Configuration config;

    // Config values
    private boolean enabled;
    private int positionX;
    private int positionY;
    private float chatScale;
    private float chatOpacity;
    private boolean mirrorOnRight;
    private int chatWidth;

    // Default values
    private static final boolean DEFAULT_ENABLED = true;
    private static final int DEFAULT_POSITION_X = 0;
    private static final int DEFAULT_POSITION_Y = 0;
    private static final float DEFAULT_CHAT_SCALE = 1.0f;
    private static final float DEFAULT_CHAT_OPACITY = 0.5f;
    private static final boolean DEFAULT_MIRROR_ON_RIGHT = true;
    private static final int DEFAULT_CHAT_WIDTH = 320;

    public ChatConfig(File configFile) {
        this.config = new Configuration(configFile);
        loadConfig();
    }

    private void loadConfig() {
        config.load();

        enabled = config.getBoolean("enabled", "general", DEFAULT_ENABLED, "Enable the guild chat mirror");
        positionX = config.getInt("positionX", "position", DEFAULT_POSITION_X, -1000, 1000, "X offset for the chat position");
        positionY = config.getInt("positionY", "position", DEFAULT_POSITION_Y, -1000, 1000, "Y offset for the chat position");
        chatScale = config.getFloat("chatScale", "appearance", DEFAULT_CHAT_SCALE, 0.1f, 3.0f, "Scale of the chat");
        chatOpacity = config.getFloat("chatOpacity", "appearance", DEFAULT_CHAT_OPACITY, 0.0f, 1.0f, "Opacity of the chat background (0.0 = transparent, 1.0 = opaque)");
        mirrorOnRight = config.getBoolean("mirrorOnRight", "position", DEFAULT_MIRROR_ON_RIGHT, "Display the chat on the right side");
        chatWidth = config.getInt("chatWidth", "appearance", DEFAULT_CHAT_WIDTH, 100, 500, "Width of the chat window");

        if (config.hasChanged()) {
            config.save();
        }
    }

    public void saveConfig() {
        config.get("general", "enabled", DEFAULT_ENABLED).set(enabled);
        config.get("position", "positionX", DEFAULT_POSITION_X).set(positionX);
        config.get("position", "positionY", DEFAULT_POSITION_Y).set(positionY);
        config.get("appearance", "chatScale", DEFAULT_CHAT_SCALE).set(chatScale);
        config.get("appearance", "chatOpacity", DEFAULT_CHAT_OPACITY).set(chatOpacity);
        config.get("position", "mirrorOnRight", DEFAULT_MIRROR_ON_RIGHT).set(mirrorOnRight);
        config.get("appearance", "chatWidth", DEFAULT_CHAT_WIDTH).set(chatWidth);

        config.save();
    }

    // Getters
    public boolean isEnabled() { return enabled; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public float getChatScale() { return chatScale; }
    public float getChatOpacity() { return chatOpacity; }
    public boolean isMirrorOnRight() { return mirrorOnRight; }
    public int getChatWidth() { return chatWidth; }

    // Setters
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setPositionX(int positionX) { this.positionX = positionX; }
    public void setPositionY(int positionY) { this.positionY = positionY; }
    public void setChatScale(float chatScale) { this.chatScale = chatScale; }
    public void setChatOpacity(float chatOpacity) { this.chatOpacity = chatOpacity; }
    public void setMirrorOnRight(boolean mirrorOnRight) { this.mirrorOnRight = mirrorOnRight; }
    public void setChatWidth(int chatWidth) { this.chatWidth = chatWidth; }
}
