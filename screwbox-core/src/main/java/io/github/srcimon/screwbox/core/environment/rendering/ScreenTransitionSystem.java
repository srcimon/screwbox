package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.*;

@Order(SystemOrder.PRESENTATION_TRANSITIONS)
public class ScreenTransitionSystem implements EntitySystem {

    private static final Archetype TRANSITIONS = Archetype.of(ScreenTransitionComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.environment().fetchAll(TRANSITIONS)) {
            final var transitionComponent = entity.get(ScreenTransitionComponent.class);
            if (transitionComponent.startTime == null) {
                transitionComponent.startTime = Time.now();
            }
            final Duration progressDuration = Duration.since(transitionComponent.startTime);
            final var progress = Percent.of(1.0 * progressDuration.nanos() / transitionComponent.duration.nanos());
            transitionComponent.transition.draw(engine.graphics().screen(), progress);
            if (progress.isMax()) {
                entity.remove(ScreenTransitionComponent.class);
            }
        }
    }

}
