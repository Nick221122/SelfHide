# PieChart

A small client-only Fabric mod for Minecraft Java 26.3.

## Command

Run `/piechart` while in a world to print the entries and percentages from the vanilla profiler's **Levels → Block Entities** section in chat.

- Automatically collects the same vanilla frame-profiler data used by the F3 pie chart.
- Does not require opening the F3 debug screen or pressing F3 + 1.
- Captures profiler results directly and does not publish background-only data to the vanilla pie-chart UI.
- Does not scan chunks or count blocks.
- Prints the vanilla profiler's child percentages relative to the Block Entities section.
- Prints only the entries the vanilla chart displays (up to its nine numbered child entries).
- No HUD, settings, coordinates, or server-side code.

## Build

Requires JDK 25. Run `gradle --no-daemon clean test build`. The mod JAR is written to `build/libs/`.

GitHub Actions runs tests and the build on pushes to `main`, pull requests, and manual workflow dispatch.

## Compatibility

- Minecraft Java Edition 26.3
- Fabric Loader 0.20.1+
- Fabric API 0.166.2+26.3
- Java 25+
