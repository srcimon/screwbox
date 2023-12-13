package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class TweenOpacitySystemTest {

    @Test
    void update_updatesOpacityOfTweeningEntities(DefaultEnvironment environment) {
        var tweenComponent = new TweenComponent(ofSeconds(4));
        tweenComponent.progress = Percent.half();

        var renderComponent = new RenderComponent();

        environment
                .addEntity(new TweenOpacityComponent(), renderComponent, tweenComponent)
                .addSystem(new TweenOpacitySystem());

        environment.update();

        assertThat(renderComponent.opacity).isEqualTo(Percent.half());
    }
}
