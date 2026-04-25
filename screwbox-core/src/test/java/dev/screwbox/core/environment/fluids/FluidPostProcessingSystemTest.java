package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.PostProcessing;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(EnvironmentExtension.class)
class FluidPostProcessingSystemTest {

    @Test
    void update_noFluids_noEffectsAdded(DefaultEnvironment environment, PostProcessing postProcessing) {
        environment.addSystem(new FluidPostProcessingSystem());

        environment.update();

        verifyNoInteractions(postProcessing);

    }
}
