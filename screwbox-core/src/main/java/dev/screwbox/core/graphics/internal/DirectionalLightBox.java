package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;

import java.awt.*;

//TODO get rid of this class?
//TODO make record
class DirectionalLightBox {

    private final Polygon polygon;
    private final Vector bottomRight;
    private final Vector bottomLeft;
    private final Line source;
    private final double distance;

    public double distance() {
        return distance;
    }

    public Line source() {
        return source;
    }

    public Vector origin() {
        return source.start();
    }

    public Vector topRight() {
        return source.end();
    }

    public Vector bottomRight() {
        return bottomRight;
    }

    public Vector bottomLeft() {
        return bottomLeft;
    }

    public DirectionalLightBox(final Line source, final double distance) {
        this.source = source;
        this.distance = distance;

        Angle angle = Angle.of(source).addDegrees(270);
        bottomRight = angle.rotateAroundCenter(source.end(), source.end().addY(distance));
        bottomLeft = angle.rotateAroundCenter(source.start(), source.start().addY(distance));
        polygon = new Polygon();
        polygon.addPoint((int) origin().x(), (int) origin().y());
        polygon.addPoint((int) topRight().x(), (int) topRight().y());
        polygon.addPoint((int) bottomRight.x(), (int) bottomRight.y());
        polygon.addPoint((int) bottomLeft.x(), (int) bottomLeft.y());
    }

    public boolean intersects(final Bounds bounds) {
        return polygon.intersects(bounds.origin().x(), bounds.origin().y(), bounds.width(), bounds.height());
    }
}
