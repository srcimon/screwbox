package dev.screwbox.playground;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.Vector.$;

public class PolygonTestApp {

    public static void main(String[] args) {
        var screwBox = ScrewBox.createEngine();
        screwBox.environment().addSystem(e -> {
            World world = e.graphics().world();
            Polygon source = Polygon.ofNodes($(-20, -30), $(5, -10), $(40, -40), $(46, 30), $(2, 60), $(-15, 30), $(-20, -30));
            Vector targetPosition = projectNode(source.center(), e.mouse().position());

            Polygon target = source.moveTo(targetPosition);
            Polygon outline = source.projectTo(targetPosition);
//            Polygon target = translateRelativeToLightSource(source, e.mouse().position());
            world.drawPolygon(source, PolygonDrawOptions.filled(Color.RED));
            world.drawPolygon(target, PolygonDrawOptions.filled(Color.ORANGE));
            world.drawPolygon(outline, PolygonDrawOptions.outline(Color.WHITE));
            world.drawLine(source.center(), target.center(), LineDrawOptions.color(Color.WHITE).strokeWidth(2));
        });
        screwBox.start();
    }

    private static Polygon translateRelativeToLightSource(final Polygon source, final Vector position) {
        List<Vector> vertices = new ArrayList<>();
        for (var node : source.definitionNotes()) {
            vertices.add(projectNode(position, node));
        }
        return Polygon.ofNodes(vertices);
    }

    private static Vector projectNode(Vector position, Vector node) {
        final double xDist = position.x() - node.x();
        final double yDist = position.y() - node.y();
        return node.add(xDist * -0.5, yDist * -0.5);
    }
}
