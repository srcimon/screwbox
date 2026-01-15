package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Renders cloth using mesh shading of soft bodies with a {@link ClothComponent}.
 *
 * @since 3.20.0
 */
@ExecutionOrder(PRESENTATION_WORLD)
public class ClothRenderSystem implements EntitySystem {

    private static final Archetype CLOTHS = Archetype.ofSpacial(ClothRenderComponent.class, ClothComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var cloth : engine.environment().fetchAll(CLOTHS)) {
            final var renderConfig = cloth.get(ClothRenderComponent.class);
            final var clothConfig = cloth.get(ClothComponent.class);
            renderCloth(engine, clothConfig, renderConfig);
        }
    }

    private static void renderCloth(final Engine engine, final ClothComponent clothConfig, final ClothRenderComponent renderConfig) {
        final Graphics graphics = engine.graphics();
        final double referenceArea = renderConfig.detailed ? clothConfig.meshCellSize.pixelCount() / 2.0 : clothConfig.meshCellSize.pixelCount();
        final double drawingDistance = Math.max(clothConfig.meshCellSize.width(), clothConfig.meshCellSize.height());
        final int meshWidth = clothConfig.mesh.length - 1;
        final int meshHeight = clothConfig.mesh[0].length - 1;
        final var frame = fetchFrame(engine, renderConfig);
        for (int x = 0; x < meshWidth; x++) {
            for (int y = 0; y < meshHeight; y++) {
                final var origin = clothConfig.mesh[x][y].position();
                final var bottomRight = clothConfig.mesh[x + 1][y + 1].position();
                if (graphics.isWithinDistanceToVisibleArea(origin, drawingDistance) || graphics.isWithinDistanceToVisibleArea(bottomRight, drawingDistance)) {
                    final var topRight = clothConfig.mesh[x + 1][y].position();
                    final var bottomLeft = clothConfig.mesh[x][y + 1].position();
                    final var frameColor = fetchFrameColor(frame, x, y);
                    if (renderConfig.detailed) {
                        final Polygon upperTriangle = Polygon.ofNodes(origin, topRight, bottomLeft, origin);
                        render(graphics, referenceArea, upperTriangle, frameColor, renderConfig);
                        final Polygon lowerTriangle = Polygon.ofNodes(bottomRight, bottomLeft, topRight, bottomRight);
                        render(graphics, referenceArea, lowerTriangle, frameColor, renderConfig);
                    } else {
                        final Polygon tetragon = Polygon.ofNodes(origin, topRight, bottomRight, bottomLeft, origin);
                        render(graphics, referenceArea, tetragon, frameColor, renderConfig);
                    }
                }
            }
        }
    }

    private static Frame fetchFrame(final Engine engine, final ClothRenderComponent renderConfig) {
        return isNull(renderConfig.texture) ? null : renderConfig.texture.frame(engine.loop().time());
    }

    private static Color fetchFrameColor(final Frame frame, final int x, final int y) {
        return isNull(frame)
            ? null
            : frame.colorAt(x % frame.size().width(), y % frame.size().height());
    }

    private static void render(final Graphics graphics, final double referenceArea, final Polygon polygon, final Color textureColor, final ClothRenderComponent config) {
        final var area = polygon.area();
        final double areaDifference = (config.sizeImpactModifier.value() * (referenceArea - area) / referenceArea + 1) / 2.0;
        final double adjustment = Percent.of(areaDifference).rangeValue(-config.brightnessRange.value(), config.brightnessRange.value());
        final Color colorToUse = adjustColor(polygon, textureColor, config);
        graphics.world().drawPolygon(polygon, PolygonDrawOptions.filled(colorToUse.adjustBrightness(adjustment)).drawOrder(config.drawOrder));
    }

    private static Color adjustColor(final Polygon polygon, final Color textureColor, final ClothRenderComponent config) {
        if (nonNull(config.backgroundColor) && !polygon.isClockwise()) {
            return config.backgroundColor;
        }
        return isNull(textureColor)
            ? config.color
            : textureColor;
    }
}
