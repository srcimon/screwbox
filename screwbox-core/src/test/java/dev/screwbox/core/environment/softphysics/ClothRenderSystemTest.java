package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class ClothRenderSystemTest {

    @Test
    void update_detailedCloth_rendersTwoPolygonsPerMeshCell(DefaultEnvironment environment, World world, Graphics graphics) {
        var cloth = SoftPhysicsSupport.createCloth(Bounds.$$(0, 0, 128, 128), Size.of(4, 8), environment);
        cloth.root().add(new ClothRenderComponent());

        when(graphics.isWithinDistanceToVisibleArea(any(), anyDouble())).thenReturn(true);
        environment
            .addEntities(cloth)
            .addSystem(new ClothRenderSystem());
        environment.update();

        verify(world, times(64)).drawPolygon(
            argThat((ArgumentMatcher<Polygon>) polygon -> polygon.isClosed() && polygon.nodeCount() == 3),
            argThat(options -> options.equals(PolygonDrawOptions.filled(Color.rgb(253, 0, 0)))));
    }

    @Test
    void update_nonDetailedTexturedCloth_rendersOnPolygonPerMeshCell(DefaultEnvironment environment, World world, Graphics graphics) {
        var cloth = SoftPhysicsSupport.createCloth(Bounds.$$(0, 0, 128, 128), Size.of(4, 8), environment);
        cloth.root().add(new ClothRenderComponent(), config -> {
            config.detailed = false;
            config.texture = SpriteBundle.BOX.get();
        });

        when(graphics.isWithinDistanceToVisibleArea(any(), anyDouble())).thenReturn(true);
        environment
            .addEntities(cloth)
            .addSystem(new ClothRenderSystem());
        environment.update();

        verify(world, times(32)).drawPolygon(any(Polygon.class), any());

        verify(world, times(5)).drawPolygon(
            argThat((ArgumentMatcher<Polygon>) polygon -> polygon.isClosed() && polygon.nodeCount() == 4),
            argThat(options -> options.equals(PolygonDrawOptions.filled(Color.rgb(35, 36, 34)))));
    }
}
