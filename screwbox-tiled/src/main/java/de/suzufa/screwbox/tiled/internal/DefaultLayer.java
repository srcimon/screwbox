package de.suzufa.screwbox.tiled.internal;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.Properties;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;

public class DefaultLayer implements Layer {

    private final LayerEntity layerEntity;

    @Override
    public String toString() {
        return "Layer [name=" + layerEntity.name() + "]";
    }

    private final int order;

    public DefaultLayer(final LayerEntity layerEntity, final int order) {
        this.layerEntity = layerEntity;
        this.order = order;
    }

    @Override
    public String name() {
        return layerEntity.name();
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public Properties properties() {
        return new DefaultProperties(layerEntity.properties());
    }

    @Override
    public Percentage opacity() {
        return Percentage.of(layerEntity.opacity());
    }

    @Override
    public boolean isImageLayer() {
        return "imagelayer".equals(layerEntity.type());
    }

    @Override
    public Optional<String> image() {
        return ofNullable(layerEntity.image());
    }

    @Override
    public double parallaxX() {
        return layerEntity.parallaxx();
    }

    @Override
    public double parallaxY() {
        return layerEntity.parallaxy();
    }

}
