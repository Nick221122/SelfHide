package dev.piechart.mixin;

import dev.piechart.PieChartClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Enables the same vanilla profiler used by F3+1 while keeping its chart hidden.
 */
@Mixin(Minecraft.class)
public abstract class MinecraftProfilerMixin {
    @ModifyVariable(method = "constructProfiler", at = @At("HEAD"), argsOnly = true)
    private boolean piechart$collectProfilerInBackground(boolean shouldCollectFrameProfile) {
        return shouldCollectFrameProfile || PieChartClient.isAutomaticCaptureActive();
    }

    @ModifyVariable(method = "finishProfilers", at = @At("HEAD"), argsOnly = true)
    private boolean piechart$publishProfilerResultsInBackground(boolean shouldCollectFrameProfile) {
        return shouldCollectFrameProfile || PieChartClient.isAutomaticCaptureActive();
    }
}
