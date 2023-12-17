package io.github.srcimon.screwbox.core.environment.setup;

import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.setup.internal.DefaultEnvironmentSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class DefaultEnvironmentSetupTest {

    DefaultEnvironment environment;

    DefaultEnvironmentSetup environmentSetup;

    @BeforeEach
    void setUp() {
        environment = new DefaultEnvironment(null);
        environmentSetup = new DefaultEnvironmentSetup(environment);
    }

    @Test
    void enablePhysics_addsPhysicsSystems() {
        environmentSetup.enablePhysics();

        assertThat(environment.systems()).hasSize(6)
                .anyMatch(system -> system.getClass().equals(AutomovementSystem.class))
                .anyMatch(system -> system.getClass().equals(GravitySystem.class))
                .anyMatch(system -> system.getClass().equals(MagnetSystem.class))
                .anyMatch(system -> system.getClass().equals(OptimizePhysicsPerformanceSystem.class))
                .anyMatch(system -> system.getClass().equals(PhysicsSystem.class))
                .anyMatch(system -> system.getClass().equals(CollisionDetectionSystem.class));
    }

    @Test
    void enableLight_addsLightSystems() {
        environmentSetup.enableLight();

        assertThat(environment.systems()).hasSize(2)
                .anyMatch(system -> system.getClass().equals(LightRenderSystem.class))
                .anyMatch(system -> system.getClass().equals(OptimizeLightPerformanceSystem.class));
    }

    @Test
    void disableLight_lightSystemsNotPresent_noException() {
        assertThatNoException().isThrownBy(() -> environmentSetup.disableLight());
    }

    @Test
    void disableLight_lightSystemsPresent_removesLightSystems() {
        environmentSetup.enableLight();
        environmentSetup.disableLight();

        assertThat(environment.systems()).isEmpty();
    }
}
