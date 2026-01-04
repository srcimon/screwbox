package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;

@ExecutionOrder(PRESENTATION_WORLD)
public class ClothRenderSystem implements EntitySystem {

    //TODO canvas().renderMesh(Mesh())
//TODO algorithm is called mesh shading!
    private static final Archetype CLOTHS = Archetype.ofSpacial(ClothRenderComponent.class, ClothComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var cloth : engine.environment().fetchAll(CLOTHS)) {
            Entity[][] mesh = cloth.get(ClothComponent.class).mesh;
            double normalArea = cloth.get(ClothComponent.class).normalSize.area();
            for (int y = 0; y < mesh.length; ++y) {
                for (int x = 0; x < mesh[y].length; ++x) {
                    if (x < mesh[y].length - 1 && y < mesh.length - 1) {
                        final Polygon polygon = Polygon.ofNodes(
                            mesh[x][y].position(),
                            mesh[x + 1][y].position(),
                            mesh[x + 1][y + 1].position(),
                            mesh[x][y + 1].position(),
                            mesh[x][y].position());
                        double areaDifference = ((normalArea - polygon.area()) / normalArea + 1) / 2.0;
                        engine.graphics().world().drawPolygon(polygon, PolygonDrawOptions.filled(Color.rgb(0, 0, Percent.of(areaDifference).rangeValue(0, 128)+128)));
                    }
                }
            }
        }
    }
}
