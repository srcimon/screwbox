package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static dev.screwbox.core.graphics.Color.ORANGE;
import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.SPLINE;
import static org.mockito.Mockito.verify;


@ExtendWith(EnvironmentExtension.class)
class RopeRenderSystemTest {

    @Test
    void update_ropePresent_rendersRope(DefaultEnvironment environment, World world) {
        Entity firstNode = new Entity(2)
                .add(new SoftLinkComponent(3))
                .add(new TransformComponent(4, 4, 0, 0));

        Entity secondNode = new Entity(3)
                .add(new TransformComponent(8, 8, 0, 0));

        Entity start = new Entity(1).name("rope-start")
                .add(new RopeComponent())
                .add(new RopeRenderComponent(ORANGE, 3))
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent(2, 2, 0, 0));

        environment
                .addSystem(new RopeSystem())
                .addSystem(new RopeRenderSystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        environment.update();

        verify(world).drawPolygon(List.of(start.position(), firstNode.position(), secondNode.position()), PolygonDrawOptions
                .outline(ORANGE)
                .strokeWidth(3)
                .smoothing(SPLINE));
    }
}