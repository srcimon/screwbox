package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class TweenShaderSystemTest {

    @Test
    void update_entityHasNoShader_noUpdate(DefaultEnvironment environment) {
        var tweenComponent = new TweenComponent(ofSeconds(4));
        tweenComponent.value = Percent.half();

        var renderComponent = new RenderComponent();

        environment
                .addEntity(new TweenShaderComponent(), renderComponent, tweenComponent)
                .addSystem(new TweenShaderSystem());

        environment.update();

        assertThat(renderComponent.options.shaderSetup()).isNull();
    }

    @Test
    void update_entityHasShader_setsShaderProgress(DefaultEnvironment environment) {
        var tweenComponent = new TweenComponent(ofSeconds(4));
        tweenComponent.value = Percent.half();

        var renderComponent = new RenderComponent();
        renderComponent.options = SpriteDrawOptions.originalSize().shaderSetup(ShaderBundle.SEAWATER.get());

        environment
                .addEntity(new TweenShaderComponent(), renderComponent, tweenComponent)
                .addSystem(new TweenShaderSystem());

        environment.update();

        assertThat(renderComponent.options.shaderSetup().progress()).isEqualTo(Percent.half());
    }
}
