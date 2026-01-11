package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.softphysics.ClothComponent;
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
            Entity[][] mesh = clothConfig.mesh;
            double normalArea = clothConfig.meshCellSize.pixelCount() / 2.0;
            var world = engine.graphics().world();
            for (int y = 0; y < mesh[0].length - 1; y++) {
                for (int x = 0; x < mesh.length - 1; x++) {
                    if (renderConfig.detailed) {
                        final Polygon polygonA = Polygon.ofNodes(
                            mesh[x][y].position(),
                            mesh[x + 1][y].position(),
                            mesh[x][y + 1].position(),
                            mesh[x][y].position());
                        render(world, normalArea, polygonA, renderConfig, x, y);

                        final Polygon polygonB = Polygon.ofNodes(
                            mesh[x + 1][y + 1].position(),
                            mesh[x][y + 1].position(),
                            mesh[x + 1][y].position(),
                            mesh[x + 1][y + 1].position());

                        render(world, normalArea, polygonB, renderConfig, x, y);
                    } else {
                        final Polygon polygon = Polygon.ofNodes(
                            mesh[x][y].position(),
                            mesh[x + 1][y].position(),
                            mesh[x + 1][y + 1].position(),
                            mesh[x][y + 1].position(),
                            mesh[x][y].position());
                        render(world, normalArea, polygon, renderConfig, x, y);
                    }
                }
            }
        }
    }

    private static void render(World world, double normalArea, Polygon polygon, ClothRenderComponent config, int x, int y) {

        double area = polygon.area();
        double areaDifference = ((normalArea - area) / normalArea + 1) / 2.0;
        var resultColor = isNull(config.texture) ? config.color
            : config.texture.frame(Time.now()).colorAt(x % config.texture.size().width(), y % config.texture.size().height());

        double adjustment = Percent.of(areaDifference).rangeValue(- config.brightnessRange.value(), config.brightnessRange.value());

        world.drawPolygon(polygon, PolygonDrawOptions.filled(resultColor.adjustBrightness(adjustment)).drawOrder(config.drawOrder));

    }
}
