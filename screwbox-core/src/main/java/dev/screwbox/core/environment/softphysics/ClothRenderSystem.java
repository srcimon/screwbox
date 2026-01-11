package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.Color;
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
        Time t = Time.now();
        for (final var cloth : engine.environment().fetchAll(CLOTHS)) {
            final var renderConfig = cloth.get(ClothRenderComponent.class);
            final var clothConfig = cloth.get(ClothComponent.class);
            final double referenceArea = clothConfig.meshCellSize.pixelCount() / (renderConfig.detailed ? 2.0 : 1.0);
            var world = engine.graphics().world();
            for (int y = 0; y < clothConfig.mesh[0].length - 1; y++) {
                for (int x = 0; x < clothConfig.mesh.length - 1; x++) {
                    var color = isNull(renderConfig.texture)
                        ? renderConfig.color
                        : renderConfig.texture.frame(t).colorAt(x % renderConfig.texture.size().width(), y % renderConfig.texture.size().height());

                    final Vector origin = clothConfig.mesh[x][y].position();
                    final Vector topRight = clothConfig.mesh[x + 1][y].position();
                    final Vector bottomRight = clothConfig.mesh[x][y + 1].position();
                    final Vector bottomLeft = clothConfig.mesh[x + 1][y + 1].position();
                    if (renderConfig.detailed) {
                        final Polygon polygonA = Polygon.ofNodes(origin, topRight, bottomRight, origin);
                        render(world, referenceArea, polygonA, color, renderConfig);

                        final Polygon polygonB = Polygon.ofNodes(bottomLeft, bottomRight, topRight, bottomLeft);

                        render(world, referenceArea, polygonB, color, renderConfig);
                    } else {
                        final Polygon polygon = Polygon.ofNodes(origin, topRight, bottomLeft, bottomRight, origin);
                        render(world, referenceArea, polygon, color, renderConfig);
                    }
                }
            }
        }
        System.out.println(Duration.since(t).nanos());
    }

    private static void render(final World world, final double normalArea, final Polygon polygon, final Color color, final ClothRenderComponent config) {
        double areaDifference = (config.sizeImpactModifier.value() * (normalArea - polygon.area()) / normalArea + 1) / 2.0;
        double adjustment = Percent.of(areaDifference).rangeValue(-config.brightnessRange.value(), config.brightnessRange.value());
        if (polygon.area() >= 1.0) {
            world.drawPolygon(polygon, PolygonDrawOptions.filled(color.adjustBrightness(adjustment)).drawOrder(config.drawOrder));
        }

    }
}
