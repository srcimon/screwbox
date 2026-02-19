package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.List;

public final class AwtMapper {

    private static final double MAGIC_SPLINE_NUMBER = 6.0;

    private AwtMapper() {
    }

    public static java.awt.Color toAwtColor(final Color color) {
        return new java.awt.Color(color.r(), color.g(), color.b(), color.opacity().rangeValue(0, 255));
    }

    public static Font toAwtFont(final SystemTextDrawOptions options) {
        final int value = options.isBold() ? Font.BOLD : Font.ROMAN_BASELINE;
        final int realValue = options.isItalic() ? value + Font.ITALIC : value;
        return new Font(options.fontName(), realValue, options.size());
    }

    public static GeneralPath toPath(final List<Offset> nodes) {
        final var path = new GeneralPath();
        final Offset firstNode = nodes.getFirst();
        path.moveTo(firstNode.x(), firstNode.y());
        for (final Offset node : nodes) {
            path.lineTo(node.x(), node.y());
        }
        return path;
    }

    public static GeneralPath toSplinePath(final List<Offset> nodes) {
        final var path = new GeneralPath();
        final Offset firstNode = nodes.getFirst();
        final boolean isCircular = nodes.getFirst().equals(nodes.getLast());
        path.moveTo(firstNode.x(), firstNode.y());
        for (int nodeNr = 0; nodeNr < nodes.size() - 1; nodeNr++) {
            if (isCircular) {
                addCircularSplinePathNode(nodes, nodeNr, path);
            } else {
                addSplinePathNode(nodes, nodeNr, path);
            }
        }
        return path;
    }

    private static void addCircularSplinePathNode(final List<Offset> nodes, final int nodeNr, final GeneralPath path) {
        final Offset currentNode = nodes.get(nodeNr);
        final Offset nextNode = nodes.get((nodeNr + 1) % nodes.size());
        final Offset previous = nodes.get((nodeNr - 1 + nodes.size() - 1) % (nodes.size() - 1));
        final Offset next = nodes.get((nodeNr + 2) % (nodes.size() - 1));
        final double leftX = currentNode.x() + (nextNode.x() - previous.x()) / MAGIC_SPLINE_NUMBER;
        final double leftY = currentNode.y() + (nextNode.y() - previous.y()) / MAGIC_SPLINE_NUMBER;
        final double rightX = nextNode.x() - (next.x() - currentNode.x()) / MAGIC_SPLINE_NUMBER;
        final double rightY = nextNode.y() - (next.y() - currentNode.y()) / MAGIC_SPLINE_NUMBER;

        path.curveTo(
            leftX, leftY, // bezier 1
            rightX, rightY,  // bezier 2
            nextNode.x(), nextNode.y()); // destination
    }

    private static void addSplinePathNode(final List<Offset> nodes, final int nodeNr, final GeneralPath path) {
        final Offset currentNode = nodes.get(nodeNr);
        final Offset nextNode = nodes.get((nodeNr + 1) % nodes.size());
        final Offset previous = nodes.get((nodeNr - 1 + nodes.size()) % nodes.size());
        final Offset next = nodes.get((nodeNr + 2) % nodes.size());

        final boolean isEnd = nodeNr >= nodes.size() - 2;
        final double leftX = nodeNr == 0 ? currentNode.x() : currentNode.x() + (nextNode.x() - previous.x()) / MAGIC_SPLINE_NUMBER;
        final double leftY = nodeNr == 0 ? currentNode.y() : currentNode.y() + (nextNode.y() - previous.y()) / MAGIC_SPLINE_NUMBER;
        final double rightX = isEnd ? nextNode.x() : nextNode.x() - (next.x() - currentNode.x()) / MAGIC_SPLINE_NUMBER;
        final double rightY = isEnd ? nextNode.y() : nextNode.y() - (next.y() - currentNode.y()) / MAGIC_SPLINE_NUMBER;

        path.curveTo(
            leftX, leftY, // bezier 1
            rightX, rightY,  // bezier 2
            nextNode.x(), nextNode.y()); // destination
    }
}
