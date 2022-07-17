package de.suzufa.screwbox.tiled.internal;

import static de.suzufa.screwbox.core.utils.ListUtil.emptyWhenNull;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.Properties;
import de.suzufa.screwbox.tiled.internal.entity.ObjectEntity;

public class DefaultGameObject implements GameObject {

    private final ObjectEntity object;
    private final Layer layer;

    @Override
    public Vector position() {
        double xCorrection = object.getWidth() / 2;
        double yCorrection = object.getGid() == 0 ? object.getHeight() / 2 : object.getHeight() / -2;
        return Vector.of(object.getX() + xCorrection, object.getY() + yCorrection);
    }

    @Override
    public String toString() {
        return "GameObject [name=" + object.getName() + "]";
    }

    public DefaultGameObject(final ObjectEntity object, final Layer layer) {
        this.object = object;
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }

    @Override
    public int id() {
        return object.getId();
    }

    @Override
    public Bounds bounds() {
        return Bounds.atPosition(position(), object.getWidth(), object.getHeight());
    }

    @Override
    public String name() {
        return object.getName();
    }

    @Override
    public Properties properties() {
        final var properties = emptyWhenNull(object.getProperties());
        return new DefaultProperties(properties);
    }

    @Override
    public Layer layer() {
        return layer;
    }

    @Override
    public String type() {
        return object.getType();
    }

}
