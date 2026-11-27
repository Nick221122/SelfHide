package dev.selfhide.mixin;

import dev.selfhide.config.SelfHideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hides only the local avatar's base skin model. Armor and other render layers
 * remain eligible to render and are controlled by their own settings.
 */
@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {
    @Inject(method = "isBodyVisible", at = @At("HEAD"), cancellable = true)
    private void selfhide$hideLocalPlayerBody(AvatarRenderState state,
                                               CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        if (SelfHideConfig.shouldHidePlayer()
                && client.player != null
                && state.id == client.player.getId()) {
            cir.setReturnValue(false);
        }
    }
}
