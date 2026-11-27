package dev.selfhide.mixin;

import dev.selfhide.config.SelfHideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Culls only the local player's complete entity render; it changes no server state. */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true, require = 0)
    private <T extends Entity> void selfhide$hideLocalPlayer(
        T entity, Frustum frustum, double cameraX, double cameraY, double cameraZ, float partialTick,
        CallbackInfoReturnable<Boolean> cir
    ) {
        Minecraft client = Minecraft.getInstance();
        if (SelfHideConfig.shouldHidePlayer() && entity == client.player) {
            cir.setReturnValue(false);
        }
    }
}
