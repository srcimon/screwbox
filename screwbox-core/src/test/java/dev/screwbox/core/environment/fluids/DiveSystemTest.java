package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
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
        diveComponent = new DiveComponent(10);

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
        assertThat(diveComponent.isDiving).isFalse();
        assertThat(floatComponent.dive).isEqualTo(0.5);
    }

    @Test
    void update_updatedDiveDepthAfterLearning_diveWillLearnAboutNewDepth(DefaultEnvironment environment) {
        environment.update();

        floatComponent.dive = 1.2;

        environment.update();

        assertThat(diveComponent.inactiveDepth).isEqualTo(1.2);
        assertThat(diveComponent.isDiving).isFalse();
        assertThat(floatComponent.dive).isEqualTo(1.2);
    }

    @Test
    void update_physicsEntityOnTop_entityWillDive(DefaultEnvironment environment) {
        environment.addEntity(new Entity().name("seafarer")
                .add(new PhysicsComponent())
                .bounds($$(0, -110, 20, 20)));

        environment.update();


        assertThat(diveComponent.inactiveDepth).isEqualTo(0.5);
        assertThat(diveComponent.isDiving).isTrue();
        assertThat(floatComponent.dive).isEqualTo(10);
    }

    @Test
    void update_physicsEntityOnTopRemoved_entityWillFloatAgain(DefaultEnvironment environment) {
        Entity seafarer = new Entity().name("seafarer")
                .add(new PhysicsComponent())
                .bounds($$(0, -110, 20, 20));

        environment.addEntity(seafarer);

        environment.update();

        assertThat(diveComponent.isDiving).isTrue();

        environment.remove(seafarer);

        environment.update();

        assertThat(diveComponent.inactiveDepth).isEqualTo(0.5);
        assertThat(diveComponent.isDiving).isTrue();
        assertThat(floatComponent.dive).isEqualTo(0.5);
    }
}
