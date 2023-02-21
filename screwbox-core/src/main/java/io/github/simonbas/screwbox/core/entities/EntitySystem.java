package io.github.simonbas.screwbox.core.entities;

import io.github.simonbas.screwbox.core.Engine;

@FunctionalInterface
public interface EntitySystem {

    void update(Engine engine);

}
