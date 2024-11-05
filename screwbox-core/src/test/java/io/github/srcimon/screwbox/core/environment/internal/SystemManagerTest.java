package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionSystem;
import io.github.srcimon.screwbox.core.environment.physics.OptimizePhysicsPerformanceSystem;
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
        systemManager.addSystem(new CollisionDetectionSystem());

        assertThat(systemManager.allSystems()).hasSize(1);
    }

    @Test
    void isSystemPresent_systemNotPresent_returnsFalse() {
        boolean result = systemManager.isSystemPresent(CollisionDetectionSystem.class);

        assertThat(result).isFalse();
    }

    @Test
    void isSystemPresent_systemPresent_returnsTrue() {
        systemManager.addSystem(new CollisionDetectionSystem());

        boolean result = systemManager.isSystemPresent(CollisionDetectionSystem.class);

        assertThat(result).isTrue();
    }

    @Test
    void addSystem_systemPriorityIsHigherThanExistingSystems_addsSystemToStart() {
        systemManager.addSystem(new CollisionDetectionSystem());
        systemManager.addSystem(new OptimizePhysicsPerformanceSystem());

        assertThat(systemManager.allSystems().getFirst()).isInstanceOf(OptimizePhysicsPerformanceSystem.class);
    }

}
