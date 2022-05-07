package de.suzufa.screwbox.examples.light;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.LightEmitterComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

public class MoveLightSourceSystem implements EntitySystem {

    private static final Archetype LIGHT = Archetype.of(LightEmitterComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        Entity light = engine.entityEngine().forcedFetchSingle(LIGHT);
        var transform = light.get(TransformComponent.class);
        transform.bounds = transform.bounds.moveTo(engine.mouse().worldPosition());
    }

}
