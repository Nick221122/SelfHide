package dev.selfhide.mixin;

import dev.selfhide.config.SelfHideConfig;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Hides the local player's first-person skin arm without suppressing held items. */
@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class FirstPersonHandsAndItemsRendererMixin {
    @Inject(method = "renderPlayerArm", at = @At("HEAD"), cancellable = true)
    private void selfhide$hideFirstPersonArm(PoseStack poseStack,
                                              SubmitNodeCollector submitNodeCollector,
                                              int lightCoords,
                                              float inverseArmHeight,
                                              float attackValue,
                                              HumanoidArm arm,
                                              PlayerRenderState playerState,
                                              CallbackInfo ci) {
        if (SelfHideConfig.shouldHidePlayer()) {
            ci.cancel();
        }
    }
}
