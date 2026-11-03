package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.Time;
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
        when(loop.time()).thenReturn(Time.now());
        when(graphics.configuration()).thenReturn(new GraphicsConfiguration(RenderingApi.OPEN_GL));
        when(loop.delta()).thenReturn(0.1);

        environment.addSystem(new SmokeRenderSystem())
            .addEntity(new Entity("wind").bounds($$(0, 0, 16, 16)).add(new WindComponent(Vector.x(50))))
            .addEntity(new Entity("obstacle").bounds($$(100, 50, 16, 16)).add(new SmokeObstacleComponent()))
            .addEntity(new Entity("point-emitter").bounds($$(20, 20, 16, 16)).add(new SmokeEmitterComponent(10, Color.RED, Vector.y(-10)), config -> config.isPointEmitter = true))
            .addEntity(new Entity("area-emitter").bounds($$(40, 40, 10, 10)).add(new SmokeEmitterComponent(10, Color.BLUE, Vector.y(-10))))
            .addEntity(new Entity("interactor").bounds($$(40, 40, 16, 16)).add(new SmokeInteractionComponent(50)).add(new PhysicsComponent(Vector.y(10))));

        environment.update();

        verify(smoke).approachTargetVelocity($$(0, 0, 16, 16), Vector.x(50), 0.1);
        verify(smoke).addObstacle($$(100, 50, 16, 16));
        verify(smoke).emit($(28, 28), Vector.y(-1), 1.0, Color.RED);
        verify(smoke).emit($$(40, 40, 10, 10), Vector.y(-1), 1.0, Color.BLUE);
        verify(smoke).push($$(40, 40, 16, 16), Vector.y(50));
        verify(smoke).render(0.1);
    }
}
