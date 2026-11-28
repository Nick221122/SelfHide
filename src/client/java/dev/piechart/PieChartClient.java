package dev.piechart;

import java.util.ArrayList;
import java.util.List;

import dev.piechart.mixin.MinecraftProfilerAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.ResultField;

public final class PieChartClient implements ClientModInitializer {
    private static final int CAPTURE_TICKS = 10;
    private static final int MAX_SEARCH_DEPTH = 32;
    private static final ProfilerCaptureWindow CAPTURE_WINDOW = new ProfilerCaptureWindow();
    private static FabricClientCommandSource pendingCommandSource;
    private static volatile ProfileResults capturedResults;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommands.literal("piechart").executes(context -> {
                if (CAPTURE_WINDOW.isActive()) {
                    context.getSource().sendError(Component.literal("PieChart is already collecting profiler data."));
                    return 0;
                }

                if (Minecraft.getInstance().level == null) {
                    context.getSource().sendError(Component.literal("PieChart: Join a world before running this command."));
                    return 0;
                }

                capturedResults = null;
                pendingCommandSource = context.getSource();
                CAPTURE_WINDOW.start(CAPTURE_TICKS);
                context.getSource().sendFeedback(Component.literal("PieChart: Collecting vanilla profiler data..."));
                return 1;
            }))
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!CAPTURE_WINDOW.tick()) {
                return;
            }

            FabricClientCommandSource source = pendingCommandSource;
            pendingCommandSource = null;
            ProfileResults results = capturedResults;
            capturedResults = null;
            if (source == null) {
                return;
            }

            if (results == null) {
                source.sendError(Component.literal("PieChart: Minecraft did not produce profiler data. Try again in a world."));
                return;
            }

            String blockEntitiesPath = findBlockEntitiesPath(results);
            if (blockEntitiesPath == null) {
                source.sendError(Component.literal("PieChart: No Block Entities section was found in the profiler data."));
                return;
            }

            List<String> entries = PieChartOutput.formatVisibleEntries(results.getTimes(blockEntitiesPath));
            source.sendFeedback(Component.literal("Block Entities:"));
            if (entries.isEmpty()) {
                source.sendFeedback(Component.literal("No entries were recorded."));
                return;
            }

            for (String entry : entries) {
                source.sendFeedback(Component.literal(entry));
            }
        });
    }

    public static boolean isAutomaticCaptureActive() {
        return CAPTURE_WINDOW.isActive();
    }

    /**
     * Called after Minecraft finishes a frame. The results are copied from the
     * same ContinuousProfiler used by the vanilla pie chart, but are deliberately
     * not assigned to ProfilerPieChart: that chart assumes its UI navigation
     * state is active and can crash when background-only results are published.
     */
    public static void captureProfilerResults(ProfileResults results) {
        if (CAPTURE_WINDOW.isActive() && results != null) {
            capturedResults = results;
        }
    }

    private static String findBlockEntitiesPath(ProfileResults results) {
        List<PieChartOutput.BlockEntitiesPathCandidate> candidates = new ArrayList<>();
        findBlockEntitiesPaths(results, "root", false, false, 0, candidates);
        return PieChartOutput.selectBlockEntitiesPath(candidates);
    }

    private static void findBlockEntitiesPaths(
        ProfileResults results,
        String path,
        boolean insideLevel,
        boolean insideTick,
        int depth,
        List<PieChartOutput.BlockEntitiesPathCandidate> candidates
    ) {
        if (depth >= MAX_SEARCH_DEPTH) {
            return;
        }

        List<ResultField> children = results.getTimes(path);
        if (children == null || children.size() <= 1) {
            return;
        }

        for (int index = 1; index < children.size(); index++) {
            ResultField child = children.get(index);
            String childPath = path + ProfileResults.PATH_SEPARATOR + child.name;
            boolean childInsideLevel = insideLevel
                || child.name.equalsIgnoreCase("level")
                || child.name.equalsIgnoreCase("levels");
            boolean childInsideTick = insideTick || child.name.equalsIgnoreCase("tick");

            if (child.name.equalsIgnoreCase("blockEntities") || child.name.equalsIgnoreCase("block entities")) {
                List<ResultField> entries = results.getTimes(childPath);
                int childCount = entries == null ? 0 : Math.max(0, entries.size() - 1);
                int typedChildCount = 0;
                if (entries != null) {
                    for (int entryIndex = 1; entryIndex < entries.size(); entryIndex++) {
                        if (!entries.get(entryIndex).name.equalsIgnoreCase("unspecified")) {
                            typedChildCount++;
                        }
                    }
                }
                candidates.add(new PieChartOutput.BlockEntitiesPathCandidate(
                    childPath, typedChildCount, childCount, childInsideTick, childInsideLevel
                ));
            }

            findBlockEntitiesPaths(results, childPath, childInsideLevel, childInsideTick, depth + 1, candidates);
        }
    }
}
