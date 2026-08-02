package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Percent;
import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.smoke.Smoke;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SmokeRenderSystemTest {

    @Test
    void update_syncsEcsWithSmoke(DefaultEnvironment environment, Smoke smoke, Loop loop, Graphics graphics) {
        when(graphics.configuration()).thenReturn(new GraphicsConfiguration(RenderingApi.OPEN_GL));
        when(loop.delta()).thenReturn(0.1);

        environment.addSystem(new SmokeRenderSystem())
            .addEntity(new Entity("wind").bounds($$(0, 0, 16, 16)).add(new WindComponent(Vector.x(50))))
            .addEntity(new Entity("obstacle").bounds($$(100, 50, 16, 16)).add(new SmokeObstacleComponent()))
            .addEntity(new Entity("emitter").bounds($$(20, 20, 16, 16)).add(new SmokeEmitterComponent(10, Color.RED, Vector.y(-10))))
            .addEntity(new Entity("interactor").bounds($$(40, 40, 16, 16)).add(new SmokeInteractionComponent(Percent.half())).add(new PhysicsComponent(Vector.y(10))));

        environment.update();

        verify(smoke).pinVelocity($$(0, 0, 16, 16), Vector.x(50));
        verify(smoke).addObstacle($$(100, 50, 16, 16));
        verify(smoke).emit($(28, 28), Vector.y(-1), 1.0, Color.RED);
        verify(smoke).push($$(40, 40, 16, 16), Vector.y(0.5));
        verify(smoke).render(0.1);
    }
}
