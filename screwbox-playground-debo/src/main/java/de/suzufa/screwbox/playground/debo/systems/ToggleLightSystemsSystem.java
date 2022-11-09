package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.systems.CreateLightSystem;
import de.suzufa.screwbox.core.entities.systems.RenderLightSystem;
import de.suzufa.screwbox.playground.debo.components.UseLightComponent;

public class ToggleLightSystemsSystem implements EntitySystem {

    private static final Archetype WORLD_INFORMATION = Archetype.of(UseLightComponent.class);

    @Override
    public void update(Engine engine) {
        Entity worldInformation = engine.entities().forcedFetch(WORLD_INFORMATION);
        boolean isCurrentlyActive = engine.entities().isSystemPresent(CreateLightSystem.class);

        if (worldInformation.get(UseLightComponent.class).useLight != isCurrentlyActive) {
            engine.entities().toggleSystem(new CreateLightSystem());
            engine.entities().toggleSystem(new RenderLightSystem());
        }
    }
}
