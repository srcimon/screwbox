package io.github.srcimon.screwbox.examples.platformer.effects;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.BackgroundComponent;
import io.github.srcimon.screwbox.tiled.Layer;

public class Background implements Converter<Layer> {

    @Override
    public Entity convert(Layer layer) {
        String imagePath = layer.image().orElseThrow().replace("../", "");
        Sprite image = Sprite.fromFile(imagePath);
        var backgroundComponent = new BackgroundComponent(
                layer.parallaxX(), layer.parallaxY(),
                layer.properties().getDouble("zoom"));

        var renderComponent = new RenderComponent(image, layer.order(), layer.opacity());
        return new Entity().add(backgroundComponent, renderComponent);
    }

}
