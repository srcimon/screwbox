package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.light.ConeLightComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.light.SpotLightComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(EnvironmentExtension.class)
class TweenLightSystemTest {

    @Test
    void update_entityHasLightComponents_updatesOpacityOfLightComponents(DefaultEnvironment environment) {
        var tweenComponent = new TweenComponent(ofSeconds(4));
        tweenComponent.value = Percent.half();

        var spotLightComponent = new SpotLightComponent(50, Color.RED);
        var pointLightComponent = new PointLightComponent(50, Color.RED);
        var coneLightComponent = new ConeLightComponent(Rotation.degrees(10), Rotation.degrees(20), 50, Color.RED);
        var glowComponent = new GlowComponent(50, Color.RED);

        environment.addSystem(new TweenLightSystem())
                .addEntity(
                        spotLightComponent,
                        pointLightComponent,
                        coneLightComponent,
                        glowComponent,
                        tweenComponent,
                        new TweenLightComponent());

        environment.update();

        assertThat(pointLightComponent.color.opacity().value()).isEqualTo(0.5);
        assertThat(spotLightComponent.color.opacity().value()).isEqualTo(0.5);
        assertThat(coneLightComponent.color.opacity().value()).isEqualTo(0.5);
        assertThat(glowComponent.color.opacity().value()).isEqualTo(0.5);
    }

    @Test
    void update_entityHasNoLightComponents_noException(DefaultEnvironment environment) {
        var tweenComponent = new TweenComponent(ofSeconds(4));
        tweenComponent.value = Percent.half();

        environment.addSystem(new TweenLightSystem())
                .addEntity(
                        tweenComponent,
                        new TweenLightComponent());

        assertThatNoException().isThrownBy(environment::update);
    }
}
