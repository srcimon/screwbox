package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.systems.RenderLightSystem;
import io.github.simonbas.screwbox.examples.platformer.components.UseLightComponent;

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
