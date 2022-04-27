package de.suzufa.screwbox.core.entityengine.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.ForwardSignalComponent;
import de.suzufa.screwbox.core.entityengine.components.RegisterToSignalComponent;

public class RegisterToSignalSystem implements EntitySystem {

    private static final Archetype UNREGISTERED = Archetype.of(RegisterToSignalComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity unregistered : engine.entityEngine().fetchAll(UNREGISTERED)) {
            var signalId = unregistered.get(RegisterToSignalComponent.class).id;

            Entity signal = engine.entityEngine().forcedFetchById(signalId);
            signal.get(ForwardSignalComponent.class).listenerIds.add(unregistered.id().orElseThrow());
            unregistered.remove(RegisterToSignalComponent.class);
        }
        if (engine.entityEngine().fetchAll(UNREGISTERED).isEmpty()) {
            engine.entityEngine().remove(RegisterToSignalSystem.class);
        }
    }
}
