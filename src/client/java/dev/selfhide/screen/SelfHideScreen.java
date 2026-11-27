package dev.selfhide.screen;

import dev.selfhide.config.SelfHideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SelfHideScreen extends Screen {
    private final Screen parent;

    public SelfHideScreen(Screen parent) {
        super(Component.literal("SelfHide Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 105;
        int y = this.height / 2 - 105;
        addToggle("SelfHide: ", () -> SelfHideConfig.enabled, value -> SelfHideConfig.enabled = value, left, y);
        addToggle("Hide entire player: ", () -> SelfHideConfig.hidePlayer, value -> SelfHideConfig.hidePlayer = value, left, y += 27);
        addToggle("Hide helmet: ", () -> SelfHideConfig.hideHelmet, value -> SelfHideConfig.hideHelmet = value, left, y += 27);
        addToggle("Hide chestplate: ", () -> SelfHideConfig.hideChestplate, value -> SelfHideConfig.hideChestplate = value, left, y += 27);
        addToggle("Hide leggings: ", () -> SelfHideConfig.hideLeggings, value -> SelfHideConfig.hideLeggings = value, left, y += 27);
        addToggle("Hide boots: ", () -> SelfHideConfig.hideBoots, value -> SelfHideConfig.hideBoots = value, left, y += 27);
        addToggle("Hide armor accessories: ", () -> SelfHideConfig.hideAccessories, value -> SelfHideConfig.hideAccessories = value, left, y += 27);
        addRenderableWidget(Button.builder(Component.literal("Done"), button -> {
            SelfHideConfig.save();
            Minecraft.getInstance().gui.setScreen(parent);
        }).bounds(left, y + 30, 210, 20).build());
    }

    private void addToggle(String label, java.util.function.BooleanSupplier getter,
                           java.util.function.Consumer<Boolean> setter, int x, int y) {
        addRenderableWidget(Button.builder(toggleText(label, getter.getAsBoolean()), button -> {
            boolean next = !getter.getAsBoolean();
            setter.accept(next);
            button.setMessage(toggleText(label, next));
            SelfHideConfig.save();
        }).bounds(x, y, 210, 20).build());
    }

    private static Component toggleText(String label, boolean value) {
        return Component.literal(label + (value ? "ON" : "OFF"));
    }

    @Override
    public void onClose() {
        SelfHideConfig.save();
        Minecraft.getInstance().setScreen(parent);
    }
}
