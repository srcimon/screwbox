package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.Engine;

@FunctionalInterface
public interface EntitySystem {

    void update(Engine engine);

}
