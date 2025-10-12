package dev.screwbox.core.environment.light;

import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Bounds.atOrigin;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class OptimizeLightPerformanceSystemTest {

    @Test
    void update_combinesHorizontallyAlignedColliders(DefaultEnvironment environment) {
        Entity brickA = new Entity().add(
                new StaticOccluderComponent(),
                new OccluderComponent(),
                new TransformComponent(atOrigin(0, 0, 20, 20)));

        Entity brickB = new Entity().add(
                new StaticOccluderComponent(),
                new OccluderComponent(),
                new TransformComponent(atOrigin(20, 0, 20, 20)));

        Entity brickC = new Entity().add(
                new StaticOccluderComponent(),
                new OccluderComponent(),
                new TransformComponent(atOrigin(40, 0, 20, 20)));

        environment.addEntities(brickA, brickB, brickC);
        environment.addSystem(new OptimizeLightPerformanceSystem());

        environment.update(); // one brick per cycle aligned
        environment.update(); // ...and another one

        var shadowCasters = environment.fetchAll(Archetype.of(OccluderComponent.class));
        var bounds = shadowCasters.getFirst().get(TransformComponent.class).bounds;
        assertThat(shadowCasters).hasSize(1);
        assertThat(bounds).isEqualTo(atOrigin(0, 0, 60, 20));
    }

    @Test
    void update_removesItselfAfterFinishingAllEntities(DefaultEnvironment environment) {
        environment.addSystem(new OptimizeLightPerformanceSystem());

        environment.update();

        assertThat(environment.systems()).isEmpty();
    }
}
