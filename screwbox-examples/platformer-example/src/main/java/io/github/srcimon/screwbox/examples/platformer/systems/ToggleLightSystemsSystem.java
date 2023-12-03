package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.ecosphere.systems.RenderLightSystem;
import io.github.srcimon.screwbox.examples.platformer.components.UseLightComponent;

public class ToggleLightSystemsSystem implements EntitySystem {

    private static final Archetype WORLD_INFORMATION = Archetype.of(UseLightComponent.class);

    @Override
    public void update(Engine engine) {
        Entity worldInformation = engine.ecosphere().forcedFetch(WORLD_INFORMATION);
        boolean isCurrentlyActive = engine.ecosphere().isSystemPresent(RenderLightSystem.class);

        if (worldInformation.get(UseLightComponent.class).useLight != isCurrentlyActive) {
            engine.ecosphere().toggleSystem(new RenderLightSystem());
        }
    }
}
