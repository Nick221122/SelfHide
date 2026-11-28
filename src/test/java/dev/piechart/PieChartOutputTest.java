package dev.piechart;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.profiling.ResultField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PieChartOutputTest {
    @Test
    void skipsThePieChartTotalAndKeepsOnlyTheNineVisibleChildren() {
        List<ResultField> pieChartEntries = new ArrayList<>();
        pieChartEntries.add(new ResultField("blockEntities", 100.0, 100.0, 1));
        for (int index = 1; index <= 11; index++) {
            pieChartEntries.add(new ResultField("minecraft:entry_" + index, index, index, 1));
        }

        List<String> output = PieChartOutput.formatVisibleEntries(pieChartEntries);

        assertEquals(9, output.size());
        assertEquals("minecraft:entry_1: 1.00%", output.get(0));
        assertEquals("minecraft:entry_9: 9.00%", output.get(8));
    }

    @Test
    void returnsNoRowsWhenTheSectionHasNoChildren() {
        List<ResultField> pieChartEntries = List.of(
            new ResultField("blockEntities", 100.0, 100.0, 1)
        );

        assertEquals(List.of(), PieChartOutput.formatVisibleEntries(pieChartEntries));
    }
}
