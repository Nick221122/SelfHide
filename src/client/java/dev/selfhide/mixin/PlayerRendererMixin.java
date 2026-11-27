package dev.selfhide.mixin;

import dev.selfhide.config.SelfHideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class PlayerRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true, require = 0)
    private void selfhide$hideLocalArmor(HumanoidRenderState state, Object poseStack,
                                          Object bufferSource, int packedLight, CallbackInfo ci) {
        var player = Minecraft.getInstance().player;
        if (player == null || state == null || state.id != player.getId()) return;

        if (SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.ACCESSORIES)
            && SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.HELMET)
            && SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.CHESTPLATE)
            && SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.LEGGINGS)
            && SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.BOOTS)) {
            ci.cancel();
        }
    }
}
