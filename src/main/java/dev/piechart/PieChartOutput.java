package dev.piechart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.util.profiling.ResultField;

public final class PieChartOutput {
    private static final int MAX_VISIBLE_CHILDREN = 9;
    private static final String MINECRAFT_NAMESPACE = "minecraft:";

    private PieChartOutput() {
    }

    /** Returns concise names only; hides the synthetic unspecified bucket, namespace and percentages. */
    public static List<String> formatVisibleEntries(List<ResultField> pieChartEntries) {
        if (pieChartEntries == null || pieChartEntries.size() <= 1) return List.of();

        List<String> output = new ArrayList<>();
        for (int index = 1; index < pieChartEntries.size() && output.size() < MAX_VISIBLE_CHILDREN; index++) {
            String name = pieChartEntries.get(index).name;
            if (name.equalsIgnoreCase("unspecified")) continue;
            if (name.startsWith(MINECRAFT_NAMESPACE)) name = name.substring(MINECRAFT_NAMESPACE.length());
            output.add(name);
        }
        return List.copyOf(output);
    }

    /** Chooses the most informative Block Entities branch when multiple profiler branches exist. */
    public static String selectBlockEntitiesPath(List<BlockEntitiesPathCandidate> candidates) {
        if (candidates == null || candidates.isEmpty()) return null;
        return candidates.stream()
            .max(Comparator.comparingInt(BlockEntitiesPathCandidate::typedChildCount)
                .thenComparing(BlockEntitiesPathCandidate::insideTick)
                .thenComparing(BlockEntitiesPathCandidate::insideLevel)
                .thenComparingInt(BlockEntitiesPathCandidate::childCount))
            .map(BlockEntitiesPathCandidate::path).orElse(null);
    }

    public record BlockEntitiesPathCandidate(String path, int typedChildCount, int childCount,
        boolean insideTick, boolean insideLevel) {}
}
