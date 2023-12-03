package io.github.srcimon.screwbox.core.ecosphere.internal;

import io.github.srcimon.screwbox.core.ecosphere.systems.CombineStaticCollidersSystem;
import io.github.srcimon.screwbox.core.ecosphere.systems.PhysicsDebugSystem;
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
        systemManager.addSystem(new PhysicsDebugSystem());

        assertThat(systemManager.allSystems()).hasSize(1);
    }

    @Test
    void isSystemPresent_systemNotPresent_returnsFalse() {
        boolean result = systemManager.isSystemPresent(PhysicsDebugSystem.class);

        assertThat(result).isFalse();
    }

    @Test
    void isSystemPresent_systemPresent_returnsTrue() {
        systemManager.addSystem(new PhysicsDebugSystem());

        boolean result = systemManager.isSystemPresent(PhysicsDebugSystem.class);

        assertThat(result).isTrue();
    }

    @Test
    void addSystem_systemPriorityIsHigherThanExistingSystems_addsSystemToStart() {
        systemManager.addSystem(new PhysicsDebugSystem());
        systemManager.addSystem(new CombineStaticCollidersSystem());

        assertThat(systemManager.allSystems().get(0)).isInstanceOf(CombineStaticCollidersSystem.class);
    }

}
