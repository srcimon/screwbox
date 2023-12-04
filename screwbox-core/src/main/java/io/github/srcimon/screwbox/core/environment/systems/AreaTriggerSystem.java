package io.github.srcimon.screwbox.core.environment.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.components.SignalComponent;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.environment.components.TriggerAreaComponent;

public class AreaTriggerSystem implements EntitySystem {

    private static final Archetype TRIGGER_AREAS = Archetype.of(
            TransformComponent.class, TriggerAreaComponent.class, SignalComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.environment().fetchAll(TRIGGER_AREAS)) {
            var triggeredBy = entity.get(TriggerAreaComponent.class).triggeredBy;
            entity.get(SignalComponent.class).isTriggered = isTriggerd(entity, triggeredBy, engine);
        }
    }

    private boolean isTriggerd(Entity entity, Archetype triggeredBy, Engine engine) {
        var areaBounds = entity.get(TransformComponent.class).bounds;
        for (var trigger : engine.environment().fetchAll(triggeredBy)) {
            var triggerBounds = trigger.get(TransformComponent.class).bounds;
            if (triggerBounds.touches(areaBounds)) {
                return true;
            }
        }
        return false;
    }

}
