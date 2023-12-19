package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.examples.platformer.components.UseLightComponent;

public class ToggleLightSystemsSystem implements EntitySystem {

    private static final Archetype WORLD_INFORMATION = Archetype.of(UseLightComponent.class);

    @Override
    public void update(Engine engine) {
        Entity worldInformation = engine.environment().forcedFetch(WORLD_INFORMATION);
        boolean isCurrentlyActive = engine.environment().isSystemPresent(LightRenderSystem.class);

        if (worldInformation.get(UseLightComponent.class).useLight != isCurrentlyActive) {
            engine.environment().toggleSystem(new LightRenderSystem());
        }
    }
}
