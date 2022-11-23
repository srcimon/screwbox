package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.ScreenTransitionComponent;

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

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_TRANSITIONS;
    }

}
