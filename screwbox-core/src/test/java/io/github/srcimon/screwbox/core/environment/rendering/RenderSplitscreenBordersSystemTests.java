package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class RenderSplitscreenBordersSystemTests {

    @Test
    void update_rendersSplitscreenBorders(DefaultEnvironment environment, Graphics graphics) {
        environment.addSystem(new RenderSplitscreenBordersSystem());
        environment.update();

        verify(graphics).renderSplitscreenBorders();
    }
}
