package dev.selfhide.mixin;

import dev.selfhide.config.SelfHideConfig;
import dev.selfhide.config.SelfHideConfig.ArmorPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class PlayerRendererMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"), require = 0)
    private void selfhide$filterLocalAvatarEquipment(Avatar entity, AvatarRenderState state,
                                                      float partialTicks, CallbackInfo ci) {
        var localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null || entity != localPlayer || state.id != localPlayer.getId()) {
            return;
        }

        if (SelfHideConfig.shouldHideArmor(ArmorPart.HELMET)) {
            state.headEquipment = ItemStack.EMPTY;
        }
        if (SelfHideConfig.shouldHideArmor(ArmorPart.CHESTPLATE)) {
            state.chestEquipment = ItemStack.EMPTY;
        }
        if (SelfHideConfig.shouldHideArmor(ArmorPart.LEGGINGS)) {
            state.legsEquipment = ItemStack.EMPTY;
        }
        if (SelfHideConfig.shouldHideArmor(ArmorPart.BOOTS)) {
            state.feetEquipment = ItemStack.EMPTY;
        }

        if (SelfHideConfig.shouldHideArmor(ArmorPart.ACCESSORIES)) {
            state.showCape = false;
            state.showExtraEars = false;
            state.parrotOnLeftShoulder = null;
            state.parrotOnRightShoulder = null;
        }
    }
}
