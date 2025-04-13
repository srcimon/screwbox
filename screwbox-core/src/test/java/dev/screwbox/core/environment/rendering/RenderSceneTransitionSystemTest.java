package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.scenes.Scenes;
import dev.screwbox.core.test.EnvironmentExtension;
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
