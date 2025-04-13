package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class TweenSpinSystemTest {

    @Test
    void update_updatesSpinOfTweeningEntities(DefaultEnvironment environment) {
        var tweenComponent = new TweenComponent(ofSeconds(4));
        tweenComponent.value = Percent.half();

        var renderComponent = new RenderComponent();

        environment
                .addEntity(new TweenSpinComponent(), renderComponent, tweenComponent)
                .addSystem(new TweenSpinSystem());

        environment.update();

        assertThat(renderComponent.options.spin()).isEqualTo(Percent.half());
    }
}
