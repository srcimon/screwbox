package dev.screwbox.core.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.smoke.Smoke;
import dev.screwbox.core.loop.internal.Updatable;

public class DefaultSmoke implements Smoke, Updatable {


    private final ViewportManager viewportManager;
    private int cellSize = 8;
    private int screenBorder=32;

    private FluidSimulation simulation;

    public DefaultSmoke(final ViewportManager viewportManager) {
        this.viewportManager = viewportManager;
    }

    @Override
    public Smoke enable() {
        Bounds visibleArea = viewportManager.defaultViewport().visibleArea();
        var size = (int)Math.max(
            Math.ceil((visibleArea.width() + 2 * screenBorder) / cellSize),
            Math.ceil((visibleArea.height() + 2 * screenBorder) / cellSize));
        simulation = new FluidSimulation(size);

        return this;
    }

    @Override
    public Smoke disable() {
        simulation = null;
        return this;
    }

    @Override
    public void update() {
        if(simulation != null) {
            //TODO get delta from update()
            simulation.step(0.02 , 0.4,0.3, 4);
        }
    }
}
