package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.postfilter.CrtMonitorPostFilter;
import dev.screwbox.core.keyboard.Key;

public class OldschoolModeSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().isPressed(Key.Y)) {
            final var postProcessing = engine.graphics().postProcessing();
            if (postProcessing.filterCount() == 0) {
                postProcessing.addViewportFilter(new CrtMonitorPostFilter());
            } else {
                postProcessing.clearFilters();
            }
        }
    }
}
