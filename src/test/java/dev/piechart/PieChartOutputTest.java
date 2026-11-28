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

    @Test
    void prefersTypedTickEntriesOverAnUnspecifiedRenderBranch() {
        List<PieChartOutput.BlockEntitiesPathCandidate> candidates = List.of(
            new PieChartOutput.BlockEntitiesPathCandidate("root\u001elevel\u001eblockEntities", 0, 1, false, true),
            new PieChartOutput.BlockEntitiesPathCandidate("root\u001elevel\u001etick\u001eentities\u001eblockEntities", 4, 5, true, true)
        );

        assertEquals(
            "root\u001elevel\u001etick\u001eentities\u001eblockEntities",
            PieChartOutput.selectBlockEntitiesPath(candidates)
        );
    }

    @Test
    void prefersTheBranchWithMoreTypedBlockEntityEntries() {
        List<PieChartOutput.BlockEntitiesPathCandidate> candidates = List.of(
            new PieChartOutput.BlockEntitiesPathCandidate("root\u001elevel\u001etick\u001eblockEntities", 2, 3, true, true),
            new PieChartOutput.BlockEntitiesPathCandidate("root\u001elevel\u001eentities\u001eblockEntities", 6, 7, false, true)
        );

        assertEquals(
            "root\u001elevel\u001eentities\u001eblockEntities",
            PieChartOutput.selectBlockEntitiesPath(candidates)
        );
    }

    @Test
    void returnsNullWhenNoBlockEntitiesBranchExists() {
        assertEquals(null, PieChartOutput.selectBlockEntitiesPath(List.of()));
    }
}
