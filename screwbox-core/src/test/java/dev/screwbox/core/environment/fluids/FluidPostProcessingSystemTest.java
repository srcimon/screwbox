package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.PostProcessing;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class FluidPostProcessingSystemTest {

    @Test
    void update_noFluids_noEffectsAdded(DefaultEnvironment environment, PostProcessing postProcessing) {
        environment.addSystem(new FluidPostProcessingSystem());

        environment.update();

        verifyNoInteractions(postProcessing);
    }

    @Test
    void update_twoFluidsNoneVisible_noEffectsAdded(DefaultEnvironment environment, PostProcessing postProcessing, Graphics graphics) {
        environment
            .addSystem(new FluidPostProcessingSystem())
            .addSystem(new FluidSystem())
            .addEntity(new Entity().bounds(Bounds.$$(0, 0, 20, 20)).add(new FluidComponent(4)).add(new FluidPostProcessingComponent()))
            .addEntity(new Entity().bounds(Bounds.$$(20, 0, 20, 20)).add(new FluidComponent(4)).add(new FluidPostProcessingComponent()));

        when(graphics.isVisible(any())).thenReturn(false);

        environment.update();

        verifyNoInteractions(postProcessing);
    }

    @Test
    void update_twoFluidsOneVisible_effectsAdded(DefaultEnvironment environment, PostProcessing postProcessing, Graphics graphics) {
        environment
            .addSystem(new FluidPostProcessingSystem())
            .addSystem(new FluidSystem())
            .addEntity(new Entity().bounds(Bounds.$$(0, 0, 20, 20)).add(new FluidComponent(4)).add(new FluidPostProcessingComponent()))
            .addEntity(new Entity().bounds(Bounds.$$(20, 0, 20, 20)).add(new FluidComponent(4)).add(new FluidPostProcessingComponent()));

        when(graphics.isVisible(any())).thenReturn(false, true);

        environment.update();

        verify(postProcessing).addEffectFilter(any());
    }
}
