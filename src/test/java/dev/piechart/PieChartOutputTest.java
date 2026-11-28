package dev.piechart;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.profiling.ResultField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PieChartOutputTest {
    @Test
    void showsOnlyShortNamesWithoutMinecraftNamespaceOrPercentages() {
        List<ResultField> entries = List.of(
            new ResultField("blockEntities", 100.0, 100.0, 1),
            new ResultField("unspecified", 63.10, 63.10, 1),
            new ResultField("minecraft:trial_spawner", 26.52, 26.52, 1),
            new ResultField("minecraft:vault", 5.59, 5.59, 1),
            new ResultField("minecraft:mob_spawner", 4.01, 4.01, 1),
            new ResultField("minecraft:chest", 0.77, 0.77, 1)
        );
        assertEquals(List.of("trial_spawner", "vault", "mob_spawner", "chest"),
            PieChartOutput.formatVisibleEntries(entries));
    }

    @Test
    void keepsNineNamedEntriesAfterFilteringUnspecified() {
        List<ResultField> entries = new ArrayList<>();
        entries.add(new ResultField("blockEntities", 100.0, 100.0, 1));
        entries.add(new ResultField("unspecified", 50.0, 50.0, 1));
        for (int i = 1; i <= 11; i++) entries.add(new ResultField("minecraft:entry_" + i, i, i, 1));
        List<String> output = PieChartOutput.formatVisibleEntries(entries);
        assertEquals(9, output.size());
        assertEquals("entry_1", output.get(0));
        assertEquals("entry_9", output.get(8));
    }

    @Test
    void preservesNonMinecraftNamespaces() {
        assertEquals(List.of("example:custom_block_entity"), PieChartOutput.formatVisibleEntries(List.of(
            new ResultField("blockEntities", 100.0, 100.0, 1),
            new ResultField("example:custom_block_entity", 100.0, 100.0, 1)
        )));
    }

    @Test
    void returnsEmptyWhenOnlyUnspecifiedIsPresent() {
        assertEquals(List.of(), PieChartOutput.formatVisibleEntries(List.of(
            new ResultField("blockEntities", 100.0, 100.0, 1),
            new ResultField("unspecified", 100.0, 100.0, 1)
        )));
    }

    @Test
    void prefersTypedTickEntriesOverAnUnspecifiedRenderBranch() {
        List<PieChartOutput.BlockEntitiesPathCandidate> candidates = List.of(
            new PieChartOutput.BlockEntitiesPathCandidate("root\u001elevel\u001eblockEntities", 0, 1, false, true),
            new PieChartOutput.BlockEntitiesPathCandidate("root\u001elevel\u001etick\u001eentities\u001eblockEntities", 4, 5, true, true)
        );
        assertEquals("root\u001elevel\u001etick\u001eentities\u001eblockEntities",
            PieChartOutput.selectBlockEntitiesPath(candidates));
    }

    @Test
    void prefersBranchWithMoreTypedEntries() {
        List<PieChartOutput.BlockEntitiesPathCandidate> candidates = List.of(
            new PieChartOutput.BlockEntitiesPathCandidate("root\u001elevel\u001etick\u001eblockEntities", 2, 3, true, true),
            new PieChartOutput.BlockEntitiesPathCandidate("root\u001elevel\u001eentities\u001eblockEntities", 6, 7, false, true)
        );
        assertEquals("root\u001elevel\u001eentities\u001eblockEntities",
            PieChartOutput.selectBlockEntitiesPath(candidates));
    }

    @Test
    void returnsNullWhenNoBranchExists() {
        assertEquals(null, PieChartOutput.selectBlockEntitiesPath(List.of()));
    }
}
