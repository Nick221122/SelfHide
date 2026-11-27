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
        int y = this.height / 2 - 35;

        addToggle("Hide player: ", () -> SelfHideConfig.hidePlayer,
            value -> SelfHideConfig.hidePlayer = value, left, y);
        addToggle("Hide armor: ", () -> SelfHideConfig.hideArmor,
            value -> SelfHideConfig.hideArmor = value, left, y + 27);

        addRenderableWidget(Button.builder(Component.literal("Done"), button -> {
            SelfHideConfig.save();
            Minecraft.getInstance().gui.setScreen(parent);
        }).bounds(left, y + 57, 210, 20).build());
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
        Minecraft.getInstance().gui.setScreen(parent);
    }
}
