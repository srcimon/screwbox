package io.github.srcimon.screwbox.core.environment.light;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Light;
import io.github.srcimon.screwbox.core.graphics.LightOptions;
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
                .addEntity(new TransformComponent($$(100, 0, 32, 32)), new SpotLightComponent(LightOptions.glowing(45)))
                .addEntity(new TransformComponent($$(200, 0, 32, 32)), new PointLightComponent(LightOptions.noGlow(20)))
                .addEntity(new TransformComponent($$(200, 0, 32, 32)), new GlowComponent(20, Color.BLUE))
                .addEntity(new TransformComponent($$(50, 50, 32, 32)), new LightBlockingComponent())
                .addSystem(new LightRenderSystem());

        environment.update();

        verify(light).addConeLight($(16,16), degrees(20), degrees(45), 30, Color.BLACK);
        verify(light).addSpotLight($(116,16), LightOptions.glowing(45));
        verify(light).addGlow($(116,16), 20, Color.BLUE);
        verify(light).addPointLight($(216,16), LightOptions.noGlow(20));
        verify(light).addShadowCaster($$(50, 50, 32, 32));
        verify(light).render();
    }
}
