package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class FixedSpinSystemTest {

    @Test
    void update_updatesSpriteSpin(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.5);

        RenderComponent renderComponent = new RenderComponent(Sprite.invisible());

        environment.addEntity(
                        renderComponent,
                        new FixedSpinComponent(0.8))
                .addSystem(new FixedSpinSystem());

        environment.update();

        Percent spin = renderComponent.options.spin();
        assertThat(spin.value()).isEqualTo(0.4);
    }

}
