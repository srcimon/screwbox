package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.systems.RenderLightSystem;
import io.github.srcimon.screwbox.examples.platformer.components.UseLightComponent;

public class ToggleLightSystemsSystem implements EntitySystem {

    private static final Archetype WORLD_INFORMATION = Archetype.of(UseLightComponent.class);

    @Override
    public void update(Engine engine) {
        Entity worldInformation = engine.entities().forcedFetch(WORLD_INFORMATION);
        boolean isCurrentlyActive = engine.entities().isSystemPresent(RenderLightSystem.class);

        if (worldInformation.get(UseLightComponent.class).useLight != isCurrentlyActive) {
            engine.entities().toggleSystem(new RenderLightSystem());
        }
    }
}
