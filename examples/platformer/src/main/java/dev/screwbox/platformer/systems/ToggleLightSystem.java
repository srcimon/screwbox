package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.platformer.components.UseLightComponent;

public class ToggleLightSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final var useLightComponent = engine.environment().fetchSingletonComponent(UseLightComponent.class);
        final var configuration = engine.graphics().configuration();
        if (configuration.isLightEnabled() != useLightComponent.useLight) {
            configuration.setLightEnabled(useLightComponent.useLight);
        }
    }
}
