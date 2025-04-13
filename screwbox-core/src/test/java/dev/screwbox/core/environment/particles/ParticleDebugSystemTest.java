package dev.screwbox.core.environment.particles;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(EnvironmentExtension.class)
class ParticleDebugSystemTest {

    @Test
    void update_drawsEmitterAndParticles(DefaultEnvironment environment, World world) {
        var disabledParticleEmitter = new ParticleEmitterComponent(Duration.ofMillis(50), ParticleOptions.unknownSource());
        disabledParticleEmitter.isEnabled = false;

        environment
                .addSystem(new ParticleDebugSystem())
                .addEntity(new TransformComponent(Vector.$(20, 20), 40, 40), new ParticleEmitterComponent(Duration.ofMillis(50), ParticleOptions.unknownSource()))
                .addEntity(new TransformComponent(Vector.$(50, 20), 40, 40), disabledParticleEmitter)
                .addEntity(new TransformComponent(Vector.$(120, 30)), new ParticleComponent(), new RenderComponent());

        environment.update();

        verify(world).drawRectangle(Bounds.atPosition(20, 20, 40, 40), RectangleDrawOptions.outline(Color.GREEN).strokeWidth(2));
        verify(world).drawRectangle(Bounds.atPosition(50, 20, 40, 40), RectangleDrawOptions.outline(Color.RED).strokeWidth(2));
        verify(world, times(2)).drawText(any(), anyString(), any(SystemTextDrawOptions.class));
        verify(world, times(4)).drawLine(any(), any());
    }
}
