package dev.selfhide.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SelfHideConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("selfhide.json");
    }

    public static boolean hidePlayer = false;
    public static boolean hideArmor = false;

    public static void load() {
        Path path = path();
        if (!Files.exists(path)) return;
        try {
            Data data = GSON.fromJson(Files.readString(path), Data.class);
            if (data == null) return;
            hidePlayer = data.hidePlayer;
            hideArmor = data.hideArmor;
        } catch (IOException | RuntimeException exception) {
            System.err.println("[SelfHide] Could not read config; using defaults: " + exception.getMessage());
        }
    }

    public static void save() {
        try {
            Path path = path();
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(new Data()));
        } catch (IOException exception) {
            System.err.println("[SelfHide] Could not save config: " + exception.getMessage());
        }
    }

    public static boolean shouldHidePlayer() {
        return hidePlayer;
    }

    public static boolean shouldHideArmor() {
        return hideArmor;
    }

    private static final class Data {
        boolean hidePlayer = SelfHideConfig.hidePlayer;
        boolean hideArmor = SelfHideConfig.hideArmor;
    }

    private SelfHideConfig() {}
}
