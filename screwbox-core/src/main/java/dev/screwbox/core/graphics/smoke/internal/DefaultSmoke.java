package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.smoke.Smoke;

public class DefaultSmoke implements Smoke {

    private boolean isEnabled = false;

    private int cellSize = 8;
    private int screenBorder=32;


    @Override
    public Smoke enable() {
        isEnabled = true;
        return this;
    }

    @Override
    public Smoke disable() {
        isEnabled = false;
        return this;
    }
}
