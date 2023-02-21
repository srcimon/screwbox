package io.github.simonbas.screwbox.tiled;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.tiled.internal.LayerEntity;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class Layer {

    private final LayerEntity layerEntity;

    @Override
    public String toString() {
        return "Layer [name=" + layerEntity.name() + "]";
    }

    private final int order;

    Layer(final LayerEntity layerEntity, final int order) {
        this.layerEntity = layerEntity;
        this.order = order;
    }

    public String name() {
        return layerEntity.name();
    }

    public int order() {
        return order;
    }

    public Properties properties() {
        return new Properties(layerEntity.properties());
    }

    public Percent opacity() {
        return Percent.of(layerEntity.opacity());
    }

    public boolean isImageLayer() {
        return "imagelayer".equals(layerEntity.type());
    }

    public Optional<String> image() {
        return ofNullable(layerEntity.image());
    }

    public double parallaxX() {
        return layerEntity.parallaxx();
    }

    public double parallaxY() {
        return layerEntity.parallaxy();
    }
}
