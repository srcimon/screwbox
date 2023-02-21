package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.ScreenTransitionComponent;

@Order(SystemOrder.PRESENTATION_TRANSITIONS)
public class ScreenTransitionSystem implements EntitySystem {

    private static final Archetype TRANSITIONS = Archetype.of(ScreenTransitionComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.entities().fetchAll(TRANSITIONS)) {
            final var transitionComponent = entity.get(ScreenTransitionComponent.class);
            if (transitionComponent.startTime == null) {
                transitionComponent.startTime = Time.now();
            }
            final Duration progressDuration = Duration.since(transitionComponent.startTime);
            final var progress = Percent.of(1.0 * progressDuration.nanos() / transitionComponent.duration.nanos());
            transitionComponent.transition.draw(engine.graphics().screen(), progress);
            if (progress.isMaxValue()) {
                entity.remove(ScreenTransitionComponent.class);
            }
        }
    }

}
