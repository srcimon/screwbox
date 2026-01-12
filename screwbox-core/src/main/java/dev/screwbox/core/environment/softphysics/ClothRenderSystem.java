package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.Objects;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;
import static java.util.Objects.isNull;

/**
 * Renders cloth using mesh shading of soft bodies with a {@link ClothComponent}.
 *
 * @since 3.20.0
 */
@ExecutionOrder(PRESENTATION_WORLD)
public class ClothRenderSystem implements EntitySystem {

    private static final Archetype CLOTHS = Archetype.ofSpacial(ClothRenderComponent.class, ClothComponent.class);

    //TODO implement backside rendering config (isClockwise)
    @Override
    public void update(Engine engine) {
        for (final var cloth : engine.environment().fetchAll(CLOTHS)) {
            final var renderConfig = cloth.get(ClothRenderComponent.class);
            final var clothConfig = cloth.get(ClothComponent.class);
            renderCloth(engine, clothConfig, renderConfig);
        }
    }

    private static void renderCloth(final Engine engine, final ClothComponent clothConfig, final ClothRenderComponent renderConfig) {
        final Graphics graphics = engine.graphics();
        final double referenceArea = clothConfig.meshCellSize.pixelCount() / (renderConfig.detailed ? 2.0 : 1.0);
        final double drawingDistance = Math.max(clothConfig.meshCellSize.width(), clothConfig.meshCellSize.height());
        final int meshWidth = clothConfig.mesh.length - 1;
        final int meshHeight = clothConfig.mesh[0].length - 1;
        final var frame = isNull(renderConfig.texture) ? null : renderConfig.texture.frame(engine.loop().time());
        for (int x = 0; x < meshWidth; x++) {
            for (int y = 0; y < meshHeight; y++) {
                final var origin = clothConfig.mesh[x][y].position();
                final var bottomRight = clothConfig.mesh[x + 1][y + 1].position();
                if (graphics.isWithinDistanceToVisibleArea(origin, drawingDistance) || graphics.isWithinDistanceToVisibleArea(bottomRight, drawingDistance)) {

                    final var topRight = clothConfig.mesh[x + 1][y].position();
                    final var bottomLeft = clothConfig.mesh[x][y + 1].position();
                    final var color = isNull(frame)
                        ? renderConfig.color
                        : frame.colorAt(x % renderConfig.texture.size().width(), y % renderConfig.texture.size().height());
                    if (renderConfig.detailed) {
                        final Polygon upperTriangle = Polygon.ofNodes(origin, topRight, bottomLeft, origin);
                        render(graphics, referenceArea, upperTriangle, color, renderConfig);
                        final Polygon lowerTriangle = Polygon.ofNodes(bottomRight, bottomLeft, topRight, bottomRight);
                        render(graphics, referenceArea, lowerTriangle, color, renderConfig);
                    } else {
                        final Polygon tetragon = Polygon.ofNodes(origin, topRight, bottomRight, bottomLeft, origin);
                        render(graphics, referenceArea, tetragon, color, renderConfig);
                    }
                }
            }
        }
    }

    private static void render(final Graphics graphics, final double referenceArea, final Polygon polygon, final Color color, final ClothRenderComponent config) {
        final var area = polygon.area();
        final double areaDifference = (config.sizeImpactModifier.value() * (referenceArea - area) / referenceArea + 1) / 2.0;
        final double adjustment = Percent.of(areaDifference).rangeValue(-config.brightnessRange.value(), config.brightnessRange.value());
        graphics.world().drawPolygon(polygon, PolygonDrawOptions.filled(color.adjustBrightness(adjustment)).drawOrder(config.drawOrder));
    }
}
