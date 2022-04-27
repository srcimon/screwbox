package de.suzufa.screwbox.core.entityengine.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.entityengine.systems.CombineStaticCollidersSystem;
import de.suzufa.screwbox.core.entityengine.systems.PhysicsDebugSystem;

class DefaultSystemManagerTest {

    private DefaultSystemManager systemManager;

    @BeforeEach
    void beforeEach() {
        systemManager = new DefaultSystemManager(null, null);
    }

    @Test
    void addSystem_addsSystem() {
        systemManager.addSystem(new PhysicsDebugSystem());

        assertThat(systemManager.getSystems()).hasSize(1);
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

        assertThat(systemManager.getSystems().get(0)).isInstanceOf(CombineStaticCollidersSystem.class);

    }

}
