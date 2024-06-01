package io.github.srcimon.screwbox.core.environment.light;

import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Light;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Rotation.degrees;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class LightRenderSystemTest {

    @Mock
    Light light;

    @Test
    void update_addsLightsAndShadowCasters(DefaultEnvironment environment, Graphics graphics) {
        when(graphics.light()).thenReturn(light);

        environment
                .addEntity(new TransformComponent($$(0, 0, 32, 32)), new ConeLightComponent(degrees(20), degrees(45), 30))
                .addEntity(new TransformComponent($$(100, 0, 32, 32)), new SpotLightComponent(45))
                .addEntity(new TransformComponent($$(200, 0, 32, 32)), new PointLightComponent(22, Color.BLUE))
                .addEntity(new TransformComponent($$(200, 0, 32, 32)), new GlowComponent(20, Color.BLUE))
                .addEntity(new TransformComponent($$(50, 50, 32, 32)), new ShadowCasterComponent())
                .addSystem(new LightRenderSystem());

        environment.update();

        verify(light).addConeLight($(16, 16), degrees(20), degrees(45), 30, Color.BLACK);
        verify(light).addSpotLight($(116, 16), 45, Color.BLACK);
        verify(light).addGlow($(216, 16), 20, Color.BLUE);
        verify(light).addPointLight($(216, 16), 22, Color.BLUE);
        verify(light).addShadowCaster($$(50, 50, 32, 32), true);
        verify(light).render();
    }
}
