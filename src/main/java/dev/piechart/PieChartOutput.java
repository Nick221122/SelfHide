package dev.piechart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.minecraft.util.profiling.ResultField;

public final class PieChartOutput {
    private static final int MAX_VISIBLE_CHILDREN = 9;

    private PieChartOutput() {
    }

    public static List<String> formatVisibleEntries(List<ResultField> pieChartEntries) {
        if (pieChartEntries == null || pieChartEntries.size() <= 1) {
            return List.of();
        }

        List<String> output = new ArrayList<>();
        int end = Math.min(pieChartEntries.size(), MAX_VISIBLE_CHILDREN + 1);
        for (int index = 1; index < end; index++) {
            ResultField entry = pieChartEntries.get(index);
            output.add(entry.name + ": " + String.format(Locale.ROOT, "%.2f%%", entry.percentage));
        }
        return List.copyOf(output);
    }

    /**
     * Chooses the most informative Block Entities branch when the profiler
     * contains both the level-render extraction branch and the level-tick branch.
     */
    public static String selectBlockEntitiesPath(List<BlockEntitiesPathCandidate> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        return candidates.stream()
            .max(Comparator
                .comparingInt(BlockEntitiesPathCandidate::typedChildCount)
                .thenComparing(BlockEntitiesPathCandidate::insideTick)
                .thenComparing(BlockEntitiesPathCandidate::insideLevel)
                .thenComparingInt(BlockEntitiesPathCandidate::childCount))
            .map(BlockEntitiesPathCandidate::path)
            .orElse(null);
    }

    public record BlockEntitiesPathCandidate(
        String path,
        int typedChildCount,
        int childCount,
        boolean insideTick,
        boolean insideLevel
    ) {
    }
}
