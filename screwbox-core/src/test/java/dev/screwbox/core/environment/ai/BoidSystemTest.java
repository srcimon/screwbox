package dev.screwbox.core.environment.ai;

import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(EnvironmentExtension.class)
class BoidSystemTest {

    @Test
    void update_noBoids_systemStopsProcessingFurther(DefaultEnvironment environment, Loop loop) {
        environment.addSystem(new BoidSystem());

        environment.update();

        verify(loop, never()).delta();
    }
}
