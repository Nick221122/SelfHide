package dev.selfhide.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SelfHideConfigTest {
    @AfterEach
    void reset() {
        SelfHideConfig.hidePlayer = false;
        SelfHideConfig.hideArmor = false;
    }

    @Test
    void bothOptionsDefaultToOff() {
        assertFalse(SelfHideConfig.shouldHidePlayer());
        assertFalse(SelfHideConfig.shouldHideArmor());
    }

    @Test
    void hidePlayerIsIndependentFromHideArmor() {
        SelfHideConfig.hidePlayer = true;
        assertTrue(SelfHideConfig.shouldHidePlayer());
        assertFalse(SelfHideConfig.shouldHideArmor());
    }

    @Test
    void hideArmorHidesAllArmorAsOneSetting() {
        SelfHideConfig.hideArmor = true;
        assertTrue(SelfHideConfig.shouldHideArmor());
        assertFalse(SelfHideConfig.shouldHidePlayer());
    }

    @Test
    void bothOptionsCanBeEnabledTogether() {
        SelfHideConfig.hidePlayer = true;
        SelfHideConfig.hideArmor = true;
        assertTrue(SelfHideConfig.shouldHidePlayer());
        assertTrue(SelfHideConfig.shouldHideArmor());
    }
}
