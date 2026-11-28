package dev.piechart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfilerCaptureWindowTest {
    @Test
    void remainsActiveForTheRequestedNumberOfTicksAndCompletesOnlyOnce() {
        ProfilerCaptureWindow window = new ProfilerCaptureWindow();
        window.start(3);

        assertTrue(window.isActive());
        assertFalse(window.tick());
        assertTrue(window.isActive());
        assertFalse(window.tick());
        assertTrue(window.isActive());
        assertTrue(window.tick());
        assertFalse(window.isActive());
        assertFalse(window.tick());
    }

    @Test
    void rejectsAnEmptyCaptureWindow() {
        ProfilerCaptureWindow window = new ProfilerCaptureWindow();

        assertThrows(IllegalArgumentException.class, () -> window.start(0));
    }
}
