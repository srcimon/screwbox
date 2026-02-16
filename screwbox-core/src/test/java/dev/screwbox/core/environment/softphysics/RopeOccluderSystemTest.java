package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.graphics.options.ShadowOptions;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Vector.$;
import static org.mockito.Mockito.verify;

@ExtendWith(EnvironmentExtension.class)
class RopeOccluderSystemTest {

    @Test
    void update_addsOccludersToRopes(DefaultEnvironment environment, Light light) {
        var softBody = SoftPhysicsSupport.createRope($(20, 10), $(20, 30), 3, environment);
        softBody.root().add(new RopeOccluderComponent(ShadowOptions.angular()));

        environment.addEntities(softBody)
            .addSystem(new RopeOccluderSystem())
            .addSystem(new RopeSystem());

        environment.update();

        verify(light).addBackgdropOccluder(Polygon.ofNodes($(19, 10), $(19, 20), $(19, 30), $(21, 30), $(21, 20), $(21, 10), $(19, 10)), ShadowOptions.angular());
    }

    @Test
    void update_hasRenderComponent_usesRenderingStrokeWidth(DefaultEnvironment environment, Light light) {
        var softBody = SoftPhysicsSupport.createRope($(20, 10), $(20, 30), 3, environment);
        softBody.root().add(new RopeOccluderComponent(ShadowOptions.angular()));
        softBody.root().add(new RopeRenderComponent(Color.BLUE, 8));

        environment.addEntities(softBody)
            .addSystem(new RopeOccluderSystem())
            .addSystem(new RopeSystem());

        environment.update();

        verify(light).addBackgdropOccluder(Polygon.ofNodes($(16, 10), $(16, 20), $(16, 30), $(24, 30), $(24, 20), $(24, 10), $(16, 10)), ShadowOptions.angular());
    }
}
