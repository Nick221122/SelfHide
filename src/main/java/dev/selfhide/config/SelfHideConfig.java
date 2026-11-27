package dev.selfhide.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SelfHideConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("selfhide.json");

    public static boolean enabled = false;
    public static boolean hidePlayer = false;
    public static boolean hideHelmet = false;
    public static boolean hideChestplate = false;
    public static boolean hideLeggings = false;
    public static boolean hideBoots = false;
    public static boolean hideAccessories = false;

    public static void load() {
        if (!Files.exists(PATH)) return;
        try {
            Data data = GSON.fromJson(Files.readString(PATH), Data.class);
            if (data == null) return;
            enabled = data.enabled;
            hidePlayer = data.hidePlayer;
            hideHelmet = data.hideHelmet;
            hideChestplate = data.hideChestplate;
            hideLeggings = data.hideLeggings;
            hideBoots = data.hideBoots;
            hideAccessories = data.hideAccessories;
        } catch (IOException | RuntimeException exception) {
            System.err.println("[SelfHide] Could not read config; using defaults: " + exception.getMessage());
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(new Data()));
        } catch (IOException exception) {
            System.err.println("[SelfHide] Could not save config: " + exception.getMessage());
        }
    }

    public static boolean shouldHidePlayer() {
        return enabled && hidePlayer;
    }

    public static boolean shouldHideArmor(ArmorPart part) {
        if (!enabled) return false;
        return switch (part) {
            case HELMET -> hideHelmet;
            case CHESTPLATE -> hideChestplate;
            case LEGGINGS -> hideLeggings;
            case BOOTS -> hideBoots;
            case ACCESSORIES -> hideAccessories;
        };
    }

    public enum ArmorPart { HELMET, CHESTPLATE, LEGGINGS, BOOTS, ACCESSORIES }

    private static final class Data {
        boolean enabled = SelfHideConfig.enabled;
        boolean hidePlayer = SelfHideConfig.hidePlayer;
        boolean hideHelmet = SelfHideConfig.hideHelmet;
        boolean hideChestplate = SelfHideConfig.hideChestplate;
        boolean hideLeggings = SelfHideConfig.hideLeggings;
        boolean hideBoots = SelfHideConfig.hideBoots;
        boolean hideAccessories = SelfHideConfig.hideAccessories;
    }

    private SelfHideConfig() {}
}
