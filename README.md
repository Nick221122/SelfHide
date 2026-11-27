# SelfHide

A client-only Fabric mod for Minecraft Java 26.3 that changes how **your own avatar** is rendered on your screen. It does not alter equipment, send gameplay packets, or change how other players see you.

## Features

- Master enable switch.
- Hide your own player model.
- Independently hide helmet, chestplate, leggings, and boots.
- Hide avatar cosmetics and accessories such as cape, shoulder parrots, and extra ears.
- Settings persist in `config/selfhide.json`.
- All options default to **OFF**.
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
