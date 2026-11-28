package dev.piechart;

import java.util.List;

import dev.piechart.mixin.ProfilerPieChartAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
import net.minecraft.network.chat.Component;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.ResultField;

public final class PieChartClient implements ClientModInitializer {
    private static final int MAX_SEARCH_DEPTH = 32;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommands.literal("piechart").executes(context -> {
                ProfilerPieChart chart = Minecraft.getInstance().getDebugOverlay().getProfilerPieChart();
                ProfileResults results = ((ProfilerPieChartAccessor) chart).piechart$getResults();

                if (results == null) {
                    context.getSource().sendFeedback(Component.literal(
                        "PieChart: Open the vanilla profiler pie chart (F3 + 1) and wait for it to collect data."
                    ));
                    return 0;
                }

                String blockEntitiesPath = findBlockEntitiesPath(results);
                if (blockEntitiesPath == null) {
                    context.getSource().sendFeedback(Component.literal(
                        "PieChart: No Block Entities section was found in the current profiler data."
                    ));
                    return 0;
                }

                List<String> entries = PieChartOutput.formatVisibleEntries(results.getTimes(blockEntitiesPath));
                if (entries.isEmpty()) {
                    context.getSource().sendFeedback(Component.literal(
                        "PieChart: The Block Entities section has no visible entries."
                    ));
                    return 0;
                }

                context.getSource().sendFeedback(Component.literal("Block Entities:"));
                for (String entry : entries) {
                    context.getSource().sendFeedback(Component.literal(entry));
                }
                return entries.size();
            }))
        );
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
