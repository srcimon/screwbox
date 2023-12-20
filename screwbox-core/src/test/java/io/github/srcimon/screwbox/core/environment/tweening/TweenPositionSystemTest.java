package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class TweenPositionSystemTest {

    @Test
    void update_updatesEntityPosition(DefaultEnvironment environment) {
        var tweenComponent = new TweenComponent(ofSeconds(4));
        tweenComponent.value = Percent.threeQuarter();

        var transformComponent = new TransformComponent(Bounds.atPosition(Vector.zero(), 20, 20));

        environment
                .addEntity(new TweenYPositionComponent(-10, 10), new TweenXPositionComponent(-40, 40), transformComponent, tweenComponent)
                .addSystem(new TweenPositionSystem());

        environment.update();

        assertThat(transformComponent.bounds).isEqualTo(Bounds.atPosition(20, 5, 20, 20));
    }
}
