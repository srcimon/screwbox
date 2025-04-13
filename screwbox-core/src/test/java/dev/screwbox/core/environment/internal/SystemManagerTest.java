package dev.screwbox.core.environment.internal;

import dev.screwbox.core.environment.physics.CollisionSensorSystem;
import dev.screwbox.core.environment.physics.OptimizePhysicsPerformanceSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SystemManagerTest {

    private SystemManager systemManager;

    @BeforeEach
    void beforeEach() {
        systemManager = new SystemManager(null, null);
    }


    @Test
    void addSystem_addsSystem() {
        systemManager.addSystem(new CollisionSensorSystem());

        assertThat(systemManager.allSystems()).hasSize(1);
    }

    @Test
    void isSystemPresent_systemNotPresent_returnsFalse() {
        boolean result = systemManager.isSystemPresent(CollisionSensorSystem.class);

        assertThat(result).isFalse();
    }

    @Test
    void isSystemPresent_systemPresent_returnsTrue() {
        systemManager.addSystem(new CollisionSensorSystem());

        boolean result = systemManager.isSystemPresent(CollisionSensorSystem.class);

        assertThat(result).isTrue();
    }

    @Test
    void addSystem_systemPriorityIsHigherThanExistingSystems_addsSystemToStart() {
        systemManager.addSystem(new CollisionSensorSystem());
        systemManager.addSystem(new OptimizePhysicsPerformanceSystem());

        assertThat(systemManager.allSystems().getFirst()).isInstanceOf(OptimizePhysicsPerformanceSystem.class);
    }

}
