package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;

public class EnvironmentFactory {

    public DefaultEnvironment createEnvironment(final Engine engine) {
        final DefaultEnvironment environment = new DefaultEnvironment(engine);
        return environment;
    }
}
