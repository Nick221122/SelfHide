package dev.piechart.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ContinuousProfiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftProfilerAccessor {
    @Accessor("fpsPieProfiler")
    ContinuousProfiler piechart$getProfiler();
}
