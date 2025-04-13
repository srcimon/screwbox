package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.platformer.components.UseLightComponent;

public class ToggleLightSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final var useLightComponent = engine.environment().fetchSingletonComponent(UseLightComponent.class);
        engine.graphics().configuration().setLightEnabled(useLightComponent.useLight);
        engine.environment().remove(ToggleLightSystem.class);
    }
}
