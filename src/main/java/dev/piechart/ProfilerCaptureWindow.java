package dev.piechart;

/**
 * Small tick-based capture window for the automatic profiler collection.
 */
public final class ProfilerCaptureWindow {
    private int remainingTicks;

    public void start(int durationTicks) {
        if (durationTicks < 1) {
            throw new IllegalArgumentException("durationTicks must be at least 1");
        }
        remainingTicks = durationTicks;
    }

    public boolean isActive() {
        return remainingTicks > 0;
    }

    /**
     * Advances the window by one client tick.
     *
     * @return true exactly once, when the capture window finishes
     */
    public boolean tick() {
        if (!isActive()) {
            return false;
        }

        remainingTicks--;
        return remainingTicks == 0;
    }
}
