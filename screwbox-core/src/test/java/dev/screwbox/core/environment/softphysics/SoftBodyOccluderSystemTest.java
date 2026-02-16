package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.graphics.options.ShadowOptions;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.verify;

@ExtendWith(EnvironmentExtension.class)
class SoftBodyOccluderSystemTest {

    @Test
    void update_addsOccludersToSoftbodies(DefaultEnvironment environment, Light light) {
        var softBody = SoftPhysicsSupport.createSoftBody(Bounds.atOrigin(20, 40, 20, 10), environment);
        softBody.root().add(new SoftBodyOccluderComponent(ShadowOptions.angular()));

        environment.addEntities(softBody)
            .addSystem(new SoftBodyOccluderSystem())
            .addSystem(new SoftBodySystem());

        environment.update();
        verify(light).addBackgdropOccluder(Polygon.fromBounds(Bounds.atOrigin(20, 40, 20, 10)), ShadowOptions.angular());
    }
}
