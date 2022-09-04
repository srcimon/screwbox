package de.suzufa.screwbox.tiled;

import static de.suzufa.screwbox.core.utils.ListUtil.emptyWhenNull;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.tiled.internal.ObjectEntity;

public class GameObject {

    private final ObjectEntity object;
    private final Layer layer;

    public Vector position() {
        double xCorrection = object.getWidth() / 2;
        double yCorrection = object.getGid() == 0 ? object.getHeight() / 2 : object.getHeight() / -2;
        return Vector.of(object.getX() + xCorrection, object.getY() + yCorrection);
    }

    @Override
    public String toString() {
        return "GameObject [name=" + object.getName() + "]";
    }

    GameObject(final ObjectEntity object, final Layer layer) {
        this.object = object;
        this.layer = layer;
    }

    public int id() {
        return object.getId();
    }

    public Bounds bounds() {
        return Bounds.atPosition(position(), object.getWidth(), object.getHeight());
    }

    public String name() {
        return object.getName();
    }

    public Properties properties() {
        final var properties = emptyWhenNull(object.getProperties());
        return new Properties(properties);
    }

    public Layer layer() {
        return layer;
    }

    public String type() {
        return object.getType();
    }
}