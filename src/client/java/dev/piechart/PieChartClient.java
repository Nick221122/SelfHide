package dev.piechart;

import java.util.List;

import dev.piechart.mixin.ProfilerPieChartAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
import net.minecraft.network.chat.Component;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.ResultField;

public final class PieChartClient implements ClientModInitializer {
    private static final int CAPTURE_TICKS = 10;
    private static final int MAX_SEARCH_DEPTH = 32;
    private static final ProfilerCaptureWindow CAPTURE_WINDOW = new ProfilerCaptureWindow();
    private static FabricClientCommandSource pendingCommandSource;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommands.literal("piechart").executes(context -> {
                if (CAPTURE_WINDOW.isActive()) {
                    context.getSource().sendError(Component.literal("PieChart is already collecting profiler data."));
                    return 0;
                }

                pendingCommandSource = context.getSource();
                CAPTURE_WINDOW.start(CAPTURE_TICKS);
                return 1;
            }))
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!CAPTURE_WINDOW.tick()) {
                return;
            }

            FabricClientCommandSource source = pendingCommandSource;
            pendingCommandSource = null;
            if (source == null) {
                return;
            }

            ProfilerPieChart chart = Minecraft.getInstance().getDebugOverlay().getProfilerPieChart();
            ProfileResults results = ((ProfilerPieChartAccessor) chart).piechart$getResults();
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
            if (entries.isEmpty()) {
                source.sendFeedback(Component.literal("Block Entities:"));
                return;
            }

            source.sendFeedback(Component.literal("Block Entities:"));
            for (String entry : entries) {
                source.sendFeedback(Component.literal(entry));
            }
        });
    }

    /**
     * Used by the Minecraft mixin to collect the vanilla profiler silently,
     * without turning on the visible F3 profiler chart.
     */
    public static boolean isAutomaticCaptureActive() {
        return CAPTURE_WINDOW.isActive();
    }

    private static String findBlockEntitiesPath(ProfileResults results) {
        SearchResult search = new SearchResult();
        findBlockEntitiesPath(results, "root", false, 0, search);
        return search.levelPath != null ? search.levelPath : search.fallbackPath;
    }

    private static void findBlockEntitiesPath(
        ProfileResults results,
        String path,
        boolean insideLevel,
        int depth,
        SearchResult search
    ) {
        if (depth >= MAX_SEARCH_DEPTH || search.levelPath != null) {
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

            if (child.name.equalsIgnoreCase("blockEntities") || child.name.equalsIgnoreCase("block entities")) {
                if (childInsideLevel && search.levelPath == null) {
                    search.levelPath = childPath;
                } else if (search.fallbackPath == null) {
                    search.fallbackPath = childPath;
                }
            }

            findBlockEntitiesPath(results, childPath, childInsideLevel, depth + 1, search);
            if (search.levelPath != null) {
                return;
            }
        }
    }

    private static final class SearchResult {
        private String levelPath;
        private String fallbackPath;
    }
}
