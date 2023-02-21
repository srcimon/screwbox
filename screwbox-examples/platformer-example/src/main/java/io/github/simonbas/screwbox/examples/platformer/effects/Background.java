package io.github.simonbas.screwbox.examples.platformer.effects;

import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.examples.platformer.components.BackgroundComponent;
import io.github.simonbas.screwbox.tiled.Layer;

public class Background implements Converter<Layer> {

    @Override
    public Entity convert(Layer layer) {
        String imagePath = layer.image().orElseThrow().replace("../", "");
        Sprite image = Sprite.fromFile(imagePath);
        var backgroundComponent = new BackgroundComponent(
                layer.parallaxX(), layer.parallaxY(),
                layer.properties().forceDouble("zoom"));

        var renderComponent = new RenderComponent(image, layer.order(), layer.opacity());
        return new Entity().add(backgroundComponent, renderComponent);
    }

}
