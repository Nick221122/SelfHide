# SelfHide

A client-only Fabric mod for Minecraft Java 26.3. It changes only how your own player is rendered on your screen. It does not change server state, send gameplay packets, or change how other players see you.

## Features

- **Hide player:** hides your complete local player model, including armor, accessories, and your first-person arm.
- **Hide armor:** hides all worn armor and supported avatar accessories together, without hiding your player model.
- Settings persist in `config/selfhide.json`.
- Press **H** to open the settings screen. The key can be changed in Options → Controls → Key Binds.

## Build

Requires JDK 25. Run:

```bash
gradle --no-daemon clean test build
```

The compiled mod is in `build/libs/`. GitHub Actions builds and tests the project on pushes to `main`, and uploads the JAR as the `SelfHide-Minecraft-26.3` workflow artifact.

## Compatibility

- Minecraft Java Edition 26.3
- Fabric Loader 0.20.1+
- Fabric API 0.166.2+26.3
- Java 25+
