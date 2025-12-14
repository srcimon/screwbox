package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static dev.screwbox.core.graphics.Color.ORANGE;
import static dev.screwbox.core.graphics.Color.RED;
import static dev.screwbox.core.graphics.Color.TRANSPARENT;
import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.NONE;
import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.SPLINE;
import static org.mockito.Mockito.verify;


@ExtendWith(EnvironmentExtension.class)
class SoftBodyRenderSystemTest {

    @Test
    void update_fillingOnly_rendersBody(DefaultEnvironment environment, World world) {
        Entity firstNode = new Entity(2)
                .add(new SoftLinkComponent(3))
                .add(new TransformComponent(4, 4, 0, 0));

        Entity secondNode = new Entity(3)
                .add(new TransformComponent(8, 8, 0, 0))
                .add(new SoftLinkComponent(1));

        Entity start = new Entity(1)
                .add(new SoftBodyComponent())
                .add(new SoftBodyRenderComponent(ORANGE))
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent(2, 2, 0, 0));

        environment
                .addSystem(new SoftBodySystem())
                .addSystem(new SoftBodyRenderSystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        environment.update();

        verify(world).drawPolygon(List.of(start.position(), firstNode.position(), secondNode.position(), start.position()), PolygonDrawOptions
                .filled(ORANGE)
                .smoothing(SPLINE));
    }

    @Test
    void update_outlineOnly_rendersBody(DefaultEnvironment environment, World world) {
        Entity firstNode = new Entity(2)
                .add(new SoftLinkComponent(3))
                .add(new TransformComponent(4, 4, 0, 0));

        Entity secondNode = new Entity(3)
                .add(new TransformComponent(8, 8, 0, 0))
                .add(new SoftLinkComponent(1));

        Entity start = new Entity(1)
                .add(new SoftBodyComponent())
                .add(new SoftBodyRenderComponent(TRANSPARENT), config -> {
                    config.rounded = false;
                    config.drawOrder = 4;
                    config.outlineColor = Color.RED;
                    config.outlineStrokeWidth = 8;
                })
                .add(new SoftLinkComponent(2))
                .add(new TransformComponent(2, 2, 0, 0));

        environment
                .addSystem(new SoftBodySystem())
                .addSystem(new SoftBodyRenderSystem())
                .addEntity(start)
                .addEntity(firstNode)
                .addEntity(secondNode);

        environment.update();

        verify(world).drawPolygon(List.of(start.position(), firstNode.position(), secondNode.position(), start.position()), PolygonDrawOptions
                .outline(RED)
                .smoothing(NONE)
                .drawOrder(4)
                .strokeWidth(8));
    }

}