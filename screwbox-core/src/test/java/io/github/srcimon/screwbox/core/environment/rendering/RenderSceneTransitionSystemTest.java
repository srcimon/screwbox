package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.scenes.Scenes;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class RenderSceneTransitionSystemTest {

    @Test
    void update_rendersSceneTransitions(DefaultEnvironment environment, Scenes scenes) {
        environment.addSystem(new RenderSceneTransitionSystem());
        environment.update();

        verify(scenes).renderTransition();
    }
}
