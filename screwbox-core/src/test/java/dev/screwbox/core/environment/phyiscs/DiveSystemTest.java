package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.DiveComponent;
import dev.screwbox.core.environment.physics.DiveSystem;
import dev.screwbox.core.environment.physics.FloatComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class DiveSystemTest {

    FloatComponent floatComponent;
    DiveComponent diveComponent;

    @BeforeEach
    void setUp(DefaultEnvironment environment) {
        floatComponent = new FloatComponent();
        diveComponent = new DiveComponent();

        environment
                .addSystem(new DiveSystem())
                .addEntity(new Entity().name("boat")
                        .add(new PhysicsComponent())
                        .add(diveComponent)
                        .add(floatComponent)
                        .bounds($$(0, -100, 40, 40)));
    }

    @Test
    void update_noOneOnBoat_diveWillLearnAboutCurrentDepth(DefaultEnvironment environment) {
        environment.update();

        assertThat(diveComponent.inactiveDepth).isEqualTo(0.5);
        assertThat(floatComponent.dive).isEqualTo(0.5);
    }

    @Test
    void update_updatedDiveDepthAfterLearning_diveWillLearnAboutNewDepth(DefaultEnvironment environment) {
        environment.update();

        floatComponent.dive = 1.2;

        environment.update();

        assertThat(diveComponent.inactiveDepth).isEqualTo(1.2);
        assertThat(floatComponent.dive).isEqualTo(1.2);
    }
}
