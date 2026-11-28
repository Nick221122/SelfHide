# PieChart

A small client-only Fabric mod for Minecraft Java 26.3.

## Command

Run `/piechart` to print the entries and percentages from the vanilla profiler's **Block Entities** section in chat.

- Automatically collects the vanilla profiler data in the background for a short capture window.
- Does not require opening the F3 debug screen or pressing F3 + 1.
- Keeps the vanilla profiler chart hidden while collecting.
- Does not scan chunks or count blocks.
- Prints the same child percentages used by the pie chart (relative to the Block Entities section).
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
