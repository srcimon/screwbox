package dev.screwbox.playground;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.Vector.$;

public class PolygonTestApp {

    public static void main(String[] args) {
        var screwBox = ScrewBox.createEngine();
        screwBox.environment().addSystem(e -> {
            Polygon source = Polygon.ofNodes($(-20, -30), $(5, -10), $(40, -40), $(46, 30), $(2, 60), $(-15, 30), $(-20, -30));
            e.graphics().world().drawPolygon(source, PolygonDrawOptions.filled(Color.RED));
            e.graphics().world().drawPolygon(translateRelativeToLightSource(source, e.mouse().position()), PolygonDrawOptions.filled(Color.YELLOW));
        });
        screwBox.start();
    }

    private static Polygon translateRelativeToLightSource(final Polygon source, final Vector position) {
        List<Vector> vertices = new ArrayList<>();

        for (var node : source.definitionNotes()) {
            final double xDist = position.x() - node.x();
            final double yDist = position.y() - node.y();
            vertices.add(node.add(xDist * -0.5, yDist * -0.5));
        }
        System.out.println(vertices);
        return Polygon.ofNodes(vertices);
    }
}
