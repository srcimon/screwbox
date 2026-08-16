package dev.screwbox.core.environment.internal;

import dev.screwbox.core.environment.physics.CollisionSensorSystem;
import dev.screwbox.core.environment.rendering.RenderSystem;
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
        systemManager.addSystem(new RenderSystem());
        systemManager.addSystem(new CollisionSensorSystem());

        assertThat(systemManager.allSystems().getFirst()).isInstanceOf(CollisionSensorSystem.class);
    }

    @Test
    void currentDrawOrder_notUpdatedYet_isZero() {
        assertThat(systemManager.currentDrawOrder()).isZero();
    }

}
