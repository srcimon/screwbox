package de.suzufa.screwbox.core.entityengine.systems;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.ScreenTransitionComponent;

public class ScreenTransitionSystem implements EntitySystem {

    private static final Archetype TRANSITIONS = Archetype.of(ScreenTransitionComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.entityEngine().fetchAll(TRANSITIONS)) {
            var transitionComponent = entity.get(ScreenTransitionComponent.class);
            if (transitionComponent.startTime == null) {
                transitionComponent.startTime = Time.now();
            }
            Duration progressDuration = Duration.since(transitionComponent.startTime);
            var progress = Percentage.of(1.0 * progressDuration.nanos() / transitionComponent.duration.nanos());
            transitionComponent.transition.draw(engine.graphics().window(), progress);
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
