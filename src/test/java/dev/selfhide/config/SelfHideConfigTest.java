package dev.selfhide.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelfHideConfigTest {
    @AfterEach
    void reset() {
        SelfHideConfig.enabled = false;
        SelfHideConfig.hidePlayer = false;
        SelfHideConfig.hideHelmet = false;
        SelfHideConfig.hideChestplate = false;
        SelfHideConfig.hideLeggings = false;
        SelfHideConfig.hideBoots = false;
        SelfHideConfig.hideAccessories = false;
    }

    @Test
    void everyVisibilityOptionDefaultsToOff() {
        reset();
        assertFalse(SelfHideConfig.enabled);
        assertFalse(SelfHideConfig.hidePlayer);
        assertFalse(SelfHideConfig.hideHelmet);
        assertFalse(SelfHideConfig.hideChestplate);
        assertFalse(SelfHideConfig.hideLeggings);
        assertFalse(SelfHideConfig.hideBoots);
        assertFalse(SelfHideConfig.hideAccessories);
    }

    @Test
    void masterSwitchOverridesEnabledIndividualOptions() {
        SelfHideConfig.hideHelmet = true;
        assertFalse(SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.HELMET));
        SelfHideConfig.enabled = true;
        assertTrue(SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.HELMET));
    }

    @Test
    void armorSlotsAreIndependent() {
        SelfHideConfig.enabled = true;
        SelfHideConfig.hideHelmet = true;
        assertTrue(SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.HELMET));
        assertFalse(SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.CHESTPLATE));
        assertFalse(SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.LEGGINGS));
        assertFalse(SelfHideConfig.shouldHideArmor(SelfHideConfig.ArmorPart.BOOTS));
    }
}
