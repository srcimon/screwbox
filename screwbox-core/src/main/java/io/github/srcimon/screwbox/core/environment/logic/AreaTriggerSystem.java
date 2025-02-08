package io.github.srcimon.screwbox.core.environment.logic;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

public class AreaTriggerSystem implements EntitySystem {

    private static final Archetype TRIGGER_AREAS = Archetype.of(
            TransformComponent.class, TriggerAreaComponent.class);

    @Override
    public void update(Engine engine) {
        for (final Entity entity : engine.environment().fetchAll(TRIGGER_AREAS)) {
            final TriggerAreaComponent trigger = entity.get(TriggerAreaComponent.class);
            trigger.isTriggered = isTriggerd(entity, trigger.triggeredBy, engine);
        }
    }

    private boolean isTriggerd(final Entity entity, final Archetype triggeredBy, final Engine engine) {
        final var areaBounds = entity.bounds();
        for (var trigger : engine.environment().fetchAll(triggeredBy)) {
            if (trigger.bounds().touches(areaBounds)) {
                return true;
            }
        }
        return false;
    }

}
