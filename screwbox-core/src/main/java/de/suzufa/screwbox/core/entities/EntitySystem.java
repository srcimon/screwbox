package de.suzufa.screwbox.core.entities;

import de.suzufa.screwbox.core.Engine;

@FunctionalInterface
public interface EntitySystem {

    void update(Engine engine);

    default UpdatePriority updatePriority() {
        return UpdatePriority.SIMULATION;
    }
}
