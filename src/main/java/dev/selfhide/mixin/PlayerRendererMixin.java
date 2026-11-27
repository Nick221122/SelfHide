package dev.selfhide.mixin;

import dev.selfhide.config.SelfHideConfig;
import dev.selfhide.config.SelfHideConfig.ArmorPart;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class PlayerRendererMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"), require = 0)
    private void selfhide$clearLocalArmorState(Object entity, HumanoidRenderState state, float tickDelta, CallbackInfo ci) {
        if (!(state instanceof PlayerRenderState playerState)) return;
        if (!selfhide$isLocalPlayer(playerState)) return;

        if (SelfHideConfig.shouldHideArmor(ArmorPart.HELMET)) {
            playerState.headEquipment = null;
        }
        if (SelfHideConfig.shouldHideArmor(ArmorPart.CHESTPLATE)) {
            playerState.chestEquipment = null;
        }
        if (SelfHideConfig.shouldHideArmor(ArmorPart.LEGGINGS)) {
            playerState.legsEquipment = null;
        }
        if (SelfHideConfig.shouldHideArmor(ArmorPart.BOOTS)) {
            playerState.feetEquipment = null;
        }
    }

    private static boolean selfhide$isLocalPlayer(PlayerRenderState state) {
        return state.id == net.minecraft.client.Minecraft.getInstance().player.getId();
    }
}
