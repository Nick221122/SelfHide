# PieChart

A small client-only Fabric mod for Minecraft Java 26.3.

## Command

Run `/piechart` to print the entries and percentages from the vanilla profiler's **Block Entities** section in chat.

- Uses the profiler data collected by Minecraft's normal pie chart; it does not scan chunks or count blocks.
- Prints the same child percentages used by the pie chart (relative to the Block Entities section).
- Prints only the entries the vanilla chart displays (up to its nine numbered child entries).
- No HUD, settings, coordinates, or server-side code.

Open the vanilla profiler pie chart with **F3 + 1**, allow it to collect profiling data, and run `/piechart`. The command finds the Block Entities node in the current profiler results; if that section has no collected entries, it reports that instead.

## Build

Requires JDK 25. Run `gradle --no-daemon clean test build`. The mod JAR is written to `build/libs/`.

GitHub Actions runs tests and the build on pushes to `main`, pull requests, and manual workflow dispatch.

## Compatibility

- Minecraft Java Edition 26.3
- Fabric Loader 0.20.1+
- Fabric API 0.166.2+26.3
- Java 25+
