package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;
import static java.util.Objects.isNull;

@ExecutionOrder(PRESENTATION_WORLD)
public class ClothRenderSystem implements EntitySystem {

    //TODO canvas().renderMesh(Mesh())
//TODO algorithm is called mesh shading!
    private static final Archetype CLOTHS = Archetype.ofSpacial(ClothRenderComponent.class, ClothComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var cloth : engine.environment().fetchAll(CLOTHS)) {
            final var renderConfig = cloth.get(ClothRenderComponent.class);
            final var clothConfig = cloth.get(ClothComponent.class);
            renderCloth(engine, clothConfig, renderConfig);
        }
    }

    private static void renderCloth(Engine engine, ClothComponent clothConfig, ClothRenderComponent renderConfig) {
        final Graphics graphics = engine.graphics();
        final double referenceArea = clothConfig.meshCellSize.pixelCount() / (renderConfig.detailed ? 2.0 : 1.0);
        final int drawingDistance = Math.max(clothConfig.meshCellSize.width(), clothConfig.meshCellSize.height());
        for (int y = 0; y < clothConfig.mesh[0].length - 1; y++) {
            for (int x = 0; x < clothConfig.mesh.length - 1; x++) {
                var color = isNull(renderConfig.texture)
                    ? renderConfig.color
                    : renderConfig.texture.frame(engine.loop().time()).colorAt(x % renderConfig.texture.size().width(), y % renderConfig.texture.size().height());

                final Vector origin = clothConfig.mesh[x][y].position();
                final Vector bottomRight = clothConfig.mesh[x + 1][y + 1].position();
                if (graphics.isWithinDistanceToVisibleArea(origin, drawingDistance) || graphics.isWithinDistanceToVisibleArea(bottomRight, drawingDistance)) {
                    final Vector topRight = clothConfig.mesh[x + 1][y].position();
                    final Vector bottomLeft = clothConfig.mesh[x][y + 1].position();

                    if (renderConfig.detailed) {
                        final Polygon polygonA = Polygon.ofNodes(origin, topRight, bottomLeft, origin);
                        render(graphics.world(), referenceArea, polygonA, color, renderConfig);

                        final Polygon polygonB = Polygon.ofNodes(bottomRight, bottomLeft, topRight, bottomRight);

                        render(graphics.world(), referenceArea, polygonB, color, renderConfig);
                    } else {
                        final Polygon polygon = Polygon.ofNodes(origin, topRight, bottomRight, bottomLeft, origin);
                        render(graphics.world(), referenceArea, polygon, color, renderConfig);
                    }
                }
            }
        }
    }

    private static void render(final World world, final double normalArea, final Polygon polygon, final Color color, final ClothRenderComponent config) {
        final double area = polygon.area();
        double areaDifference = (config.sizeImpactModifier.value() * (normalArea - area) / normalArea + 1) / 2.0;
        double adjustment = Percent.of(areaDifference).rangeValue(-config.brightnessRange.value(), config.brightnessRange.value());
        if (area >= 1.0) {
            world.drawPolygon(polygon, PolygonDrawOptions.filled(color.adjustBrightness(adjustment)).drawOrder(config.drawOrder));
        }
    }
}
