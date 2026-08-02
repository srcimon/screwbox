package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.smoke.Smoke;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SmokeRenderSystemTest {

    @Test
    void xxx(DefaultEnvironment environment, Smoke smoke, Loop loop, Graphics graphics) {
        when(graphics.configuration()).thenReturn(new GraphicsConfiguration(RenderingApi.OPEN_GL));
        when(loop.delta()).thenReturn(0.1);
        environment.addSystem(new SmokeRenderSystem());

        environment.update();

        verify(smoke).render(0.1);
        //TODO finish up
    }
}
