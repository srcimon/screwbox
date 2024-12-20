package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.platformer.components.UseLightComponent;

public class ToggleLightSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final var useLightComponent = engine.environment().fetchSingletonComponent(UseLightComponent.class);
        engine.graphics().configuration().setLightEnabled(useLightComponent.useLight);
        engine.environment().remove(ToggleLightSystem.class);
    }
}
