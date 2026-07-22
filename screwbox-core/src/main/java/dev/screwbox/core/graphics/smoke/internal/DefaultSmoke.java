package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.smoke.Smoke;

public class DefaultSmoke implements Smoke {


    private int cellSize = 8;
    private int screenBorder=32;

    private FluidSimulation simulation;

    @Override
    public Smoke enable() {
        simulation = new FluidSimulation(8);

        return this;
    }

    @Override
    public Smoke disable() {
        simulation = null;
        return this;
    }
}
