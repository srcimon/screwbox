package io.github.srcimon.screwbox.core.environment.setup;

import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.physics.*;

/**
 * The {@link EnvironmentSetup} provides a simple way to setup routine features in the {@link Environment}.
 */
public interface EnvironmentSetup {

    /**
     * Adds all default systems needed for physics support in this {@link Environment}.
     *
     * @see AutomovementSystem
     * @see CollisionDetectionSystem
     * @see GravitySystem
     * @see MagnetSystem
     * @see OptimizePhysicsPerformanceSystem
     * @see PhysicsSystem
     */
    EnvironmentSetup enablePhysics();
}
