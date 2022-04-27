package de.suzufa.screwbox.tiled.internal;

import java.util.Optional;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.Properties;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;

public class DefaultLayer implements Layer {

    private final LayerEntity layerEntity;

    @Override
    public String toString() {
        return "Layer [name=" + layerEntity.getName() + "]";
    }

    private final int order;

    public DefaultLayer(final LayerEntity layerEntity, final int order) {
        this.layerEntity = layerEntity;
        this.order = order;
    }

    @Override
    public String name() {
        return layerEntity.getName();
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public Properties properties() {
        return new DefaultProperties(layerEntity.getProperties());
    }

    @Override
    public Percentage opacity() {
        return Percentage.of(layerEntity.getOpacity());
    }

    @Override
    public boolean isImageLayer() {
        return "imagelayer".equals(layerEntity.getType());
    }

    @Override
    public Optional<String> image() {
        return layerEntity.getImage() == null
                ? Optional.empty()
                : Optional.of(layerEntity.getImage());
    }

    @Override
    public double parallaxX() {
        return layerEntity.getParallaxx();
    }

    @Override
    public double parallaxY() {
        return layerEntity.getParallaxy();
    }

}
