package io.github.srcimon.screwbox.core.environment.setup.internal;

import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.debug.AutomovementDebugSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.setup.EnvironmentSetup;

public class DefaultEnvironmentSetup implements EnvironmentSetup {

    private final Environment environment;

    public DefaultEnvironmentSetup(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public EnvironmentSetup enablePhysics() {
        environment.addSystems(
                new AutomovementDebugSystem(),
                new CollisionDetectionSystem(),
                new GravitySystem(),
                new MagnetSystem(),
                new OptimizePhysicsPerformanceSystem(),
                new PhysicsSystem());
        return this;
    }
}
