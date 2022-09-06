package de.suzufa.screwbox.core.entityengine.systems;

import java.util.List;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WaterComponent;
import de.suzufa.screwbox.core.entityengine.components.WaterReflectionComponent;

public class WaterReflectionSystem implements EntitySystem {

    private static final Archetype WATERS = Archetype.of(WaterComponent.class, TransformComponent.class);
    private static final Archetype RELECTED_ENTITIES = Archetype.of(
            WaterReflectionComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        List<Entity> reflectedEntities = engine.entityEngine().fetchAll(RELECTED_ENTITIES);

        for (Entity waters : engine.entityEngine().fetchAll(WATERS)) {

        }
    }

}
