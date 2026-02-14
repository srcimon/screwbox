package dev.screwbox.playground;

import dev.screwbox.core.Line;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import static dev.screwbox.core.Vector.$;

public class PolygonTestApp {

    public static void main(String[] args) {
        var screwBox = ScrewBox.createEngine();
        screwBox.environment().addSystem(e -> {
            World world = e.graphics().world();
            Polygon source = Polygon.ofNodes($(-20, -30), $(5, -10), $(40, -40), $(46, 30), $(2, 60), $(-15, 30), $(-20, -30));
            Vector targetPosition = projectNode(source.center(), e.mouse().position());

            Polygon target = source.moveTo(targetPosition);
            Polygon outline = source.join(target);
            world.drawPolygon(source, PolygonDrawOptions.filled(Color.RED.opacity(0.3)));
            world.drawPolygon(target, PolygonDrawOptions.filled(Color.ORANGE.opacity(0.3)));
            world.drawPolygon(outline, PolygonDrawOptions.outline(Color.WHITE.opacity(0.5)));

        });
        screwBox.start();
    }

    private static Vector projectNode(Vector position, Vector node) {
        final double xDist = position.x() - node.x();
        final double yDist = position.y() - node.y();
        return node.add(xDist * -0.5, yDist * -0.5);
    }
}
