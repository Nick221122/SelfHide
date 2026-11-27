package dev.selfhide;

import dev.selfhide.config.SelfHideConfig;
import dev.selfhide.screen.SelfHideScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class SelfHideClient implements ClientModInitializer {
    public static final String MOD_ID = "selfhide";
    private static KeyMapping openSettingsKey;

    @Override
    public void onInitializeClient() {
        SelfHideConfig.load();
        openSettingsKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.selfhide.settings", GLFW.GLFW_KEY_H, KeyMapping.Category.MISC)
        );
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettingsKey.consumeClick()) {
                if (client.player != null && client.screen == null) {
                    client.setScreen(new SelfHideScreen(client.screen));
                }
            }
        });
    }

    private SelfHideClient() {}
}
