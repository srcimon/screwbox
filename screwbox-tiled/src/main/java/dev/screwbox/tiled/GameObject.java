package dev.screwbox.tiled;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.tiled.internal.ObjectEntity;

public class GameObject {

    private final ObjectEntity object;
    private final Layer layer;

    public Vector position() {
        final double xCorrection = object.width() / 2;
        final double yCorrection = object.gid() == 0 ? object.height() / 2 : object.height() / -2;
        return Vector.of(object.x() + xCorrection, object.y() + yCorrection);
    }

    @Override
    public String toString() {
        return "GameObject [name=" + object.name() + "]";
    }

    GameObject(final ObjectEntity object, final Layer layer) {
        this.object = object;
        this.layer = layer;
    }

    public int id() {
        return object.id();
    }

    public Bounds bounds() {
        return Bounds.atPosition(position(), object.width(), object.height());
    }

    public String name() {
        return object.name();
    }

    public Properties properties() {
        return new Properties(object.properties());
    }

    public Layer layer() {
        return layer;
    }
}