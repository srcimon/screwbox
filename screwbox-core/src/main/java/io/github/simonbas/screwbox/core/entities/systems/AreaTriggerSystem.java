package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.components.SignalComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.entities.components.TriggerAreaComponent;

public class AreaTriggerSystem implements EntitySystem {

    private static final Archetype TRIGGER_AREAS = Archetype.of(
            TransformComponent.class, TriggerAreaComponent.class, SignalComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.entities().fetchAll(TRIGGER_AREAS)) {
            var triggeredBy = entity.get(TriggerAreaComponent.class).triggeredBy;
            entity.get(SignalComponent.class).isTriggered = isTriggerd(entity, triggeredBy, engine);
        }
    }

    private boolean isTriggerd(Entity entity, Archetype triggeredBy, Engine engine) {
        var areaBounds = entity.get(TransformComponent.class).bounds;
        for (var trigger : engine.entities().fetchAll(triggeredBy)) {
            var triggerBounds = trigger.get(TransformComponent.class).bounds;
            if (triggerBounds.touches(areaBounds)) {
                return true;
            }
        }
        return false;
    }

}
