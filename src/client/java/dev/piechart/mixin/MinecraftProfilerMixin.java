package dev.piechart.mixin;

import dev.piechart.PieChartClient;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ContinuousProfiler;
import net.minecraft.util.profiling.SingleTickProfiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Collects vanilla frame-profiler data without publishing it to the visible
 * vanilla pie-chart widget while the chart is inactive.
 */
@Mixin(Minecraft.class)
public abstract class MinecraftProfilerMixin {
    @ModifyVariable(method = "constructProfiler", at = @At("HEAD"), argsOnly = true)
    private boolean piechart$collectProfilerInBackground(boolean shouldCollectFrameProfile) {
        return shouldCollectFrameProfile || PieChartClient.isAutomaticCaptureActive();
    }

    @Inject(method = "finishProfilers", at = @At("HEAD"))
    private void piechart$captureProfilerResults(
        boolean shouldCollectFrameProfile,
        SingleTickProfiler tickProfiler,
        CallbackInfo ci
    ) {
        if (!PieChartClient.isAutomaticCaptureActive()) {
            return;
        }

        ContinuousProfiler profiler = ((MinecraftProfilerAccessor) this).piechart$getProfiler();
        PieChartClient.captureProfilerResults(profiler.getResults());
    }
}
