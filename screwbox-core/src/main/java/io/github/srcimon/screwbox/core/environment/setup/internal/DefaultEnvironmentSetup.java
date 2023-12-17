package io.github.srcimon.screwbox.core.environment.setup.internal;

import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.setup.EnvironmentSetup;

public class DefaultEnvironmentSetup implements EnvironmentSetup {

    private final Environment environment;

    public DefaultEnvironmentSetup(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public EnvironmentSetup enablePhysics() {
        environment.addOrReplaceSystem(new AutomovementSystem());
        environment.addOrReplaceSystem(new CollisionDetectionSystem());
        environment.addOrReplaceSystem(new GravitySystem());
        environment.addOrReplaceSystem(new MagnetSystem());
        environment.addOrReplaceSystem(new OptimizePhysicsPerformanceSystem());
        environment.addOrReplaceSystem(new PhysicsSystem());
        return this;
    }
}
