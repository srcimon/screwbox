package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.ForwardSignalComponent;
import io.github.srcimon.screwbox.core.entities.components.RegisterToSignalComponent;

public class RegisterToSignalSystem implements EntitySystem {

    private static final Archetype UNREGISTERED = Archetype.of(RegisterToSignalComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity unregistered : engine.entities().fetchAll(UNREGISTERED)) {
            var signalId = unregistered.get(RegisterToSignalComponent.class).id;

            Entity signal = engine.entities().forcedFetchById(signalId);
            signal.get(ForwardSignalComponent.class).listenerIds.add(unregistered.id().orElseThrow());
            unregistered.remove(RegisterToSignalComponent.class);
        }
        if (engine.entities().fetchAll(UNREGISTERED).isEmpty()) {
            engine.entities().remove(RegisterToSignalSystem.class);
        }
    }
}
