package dev.screwbox.tiled;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.tiled.internal.ObjectEntity;

public class GameObject {

    private final ObjectEntity object;
    private final Layer layer;
    private final Properties properties;

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
        this.properties = new Properties(object.properties());
    }

    /**
     * Returns the id of the game object.
     *
     * @since 3.42.0
     */
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
        return properties;
    }

    public Layer layer() {
        return layer;
    }

    /**
     * Returns {@code true} if the object contains a property with the specified name.
     *
     * @since 3.42.0
     */
    public boolean hasProperty(final String name) {
        return properties().hasProperty(name);
    }
}