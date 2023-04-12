package io.github.srcimon.screwbox.core.entities;

import io.github.srcimon.screwbox.core.Engine;

@FunctionalInterface
public interface EntitySystem {

    void update(Engine engine);

}
