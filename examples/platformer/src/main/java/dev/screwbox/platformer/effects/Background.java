package dev.screwbox.platformer.effects;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.BackgroundComponent;
import dev.screwbox.tiled.Layer;

import static dev.screwbox.core.graphics.options.SpriteDrawOptions.originalSize;

public class Background implements Converter<Layer> {

    @Override
    public Entity convert(Layer layer) {
        String imagePath = layer.image().orElseThrow().replace("../", "");
        Sprite image = Sprite.fromFile(imagePath).prepareShader(ShaderBundle.BREEZE);
        var backgroundComponent = new BackgroundComponent(
                layer.parallaxX(), layer.parallaxY(),
                layer.properties().getDouble("zoom"));

        var renderComponent = new RenderComponent(image, layer.order(), originalSize().opacity(layer.opacity()));
        return new Entity().add(backgroundComponent, renderComponent);
    }

}
