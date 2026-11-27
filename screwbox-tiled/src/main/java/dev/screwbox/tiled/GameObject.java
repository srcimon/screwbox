package dev.screwbox.tiled;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.utils.Validate;
import dev.screwbox.tiled.internal.ObjectEntity;
import dev.screwbox.tiled.internal.PropertyEntity;

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

    /**
     * Adds a new property.
     *
     * @throws IllegalArgumentException when property already exists
     * @since 4.42.0
     */
    public void addProperty(final String name, final String value) {
        Validate.isFalse(hasProperty(name), "property '%s' already exists".formatted(name));
        object.properties().add(new PropertyEntity(name, "string", value));
    }

    public Properties properties() {
        return new Properties(object.properties());
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