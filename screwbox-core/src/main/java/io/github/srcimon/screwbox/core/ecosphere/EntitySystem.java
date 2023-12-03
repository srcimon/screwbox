package io.github.srcimon.screwbox.core.ecosphere;

import io.github.srcimon.screwbox.core.Engine;

@FunctionalInterface
public interface EntitySystem {

    void update(Engine engine);

}
