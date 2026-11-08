package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class FloatRotationSystemTest {

    @Test
    void update_floatingEntityPresent_rotatesRenderComponentToWave(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.01);
        FloatComponent floatComponent = new FloatComponent();
        floatComponent.attachedWave = Line.between($(-16, 16), $(32, 329));

        RenderComponent renderComponent = new RenderComponent();
        environment
            .addSystem(new FloatRotationSystem())
            .addEntity(new Entity().name("boat")
                .bounds(Bounds.atOrigin(32, -16, 64, 32))
                .add(renderComponent)
                .add(new FloatRotationComponent())
                .add(floatComponent)
            );

        environment.update();

        assertThat(renderComponent.options.rotation().degrees()).isEqualTo(4.88, Offset.offset(0.01));

        environment.update();

        assertThat(renderComponent.options.rotation().degrees()).isEqualTo(9.46, Offset.offset(0.01));

        environment.updateTimes(20);

        assertThat(renderComponent.options.rotation().degrees()).isEqualTo(60.45, Offset.offset(0.01));
    }
}
