package dev.screwbox.core.environment.light;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Line;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.screwbox.core.Angle.degrees;
import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
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
            .addEntity(new TransformComponent($$(50, 0, 32, 32)), new AreaLightComponent(Color.BLUE))
            .addEntity(new TransformComponent($$(200, 0, 32, 32)), new GlowComponent(20, Color.BLUE))
            .addEntity(new TransformComponent($$(50, 50, 32, 32)), new OccluderComponent())
            .addEntity(new TransformComponent($$(50, 50, 32, 32)), new AreaGlowComponent(30, Color.BLUE))
            .addEntity(new TransformComponent($$(50, 50, 32, 32)), new ConeGlowComponent(Angle.degrees(20), Angle.degrees(120), 30, Color.BLUE))
            .addEntity(new TransformComponent($$(500, 50, 32, 32)), new OrthographicWallComponent())
            .addEntity(new Entity().bounds($$(500, 50, 32, 32)).add(new DirectionalLightComponent(), light -> light.angle = Angle.degrees(90)))
            .addSystem(new LightRenderSystem());

        environment.update();

        verify(light).addConeLight($(16, 16), degrees(20), degrees(45), 30, Color.BLACK);
        verify(light).addSpotLight($(116, 16), 45, Color.BLACK);
        verify(light).addGlow($(216, 16), 20, Color.BLUE, null);
        verify(light).addPointLight($(216, 16), 22, Color.BLUE);
        verify(light).addAreaLight($$(50, 0, 32, 32), Color.BLUE, 0, false);
        verify(light).addAreaGlow($$(50, 50, 32, 32), 30.0, Color.BLUE, null);
        verify(light).addOccluder($$(50, 50, 32, 32), true);
        verify(light).addConeGlow($(66, 66), Angle.degrees(20), Angle.degrees(120), 30, Color.BLUE);
        verify(light).addOrthographicWall($$(500, 50, 32, 32));
        verify(light).addDirectionalLight(Line.between($(516, 34), $(516, 66)), 32, Color.BLACK);
        verify(light).render();
    }
}
