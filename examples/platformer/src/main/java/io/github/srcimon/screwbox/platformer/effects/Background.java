package io.github.srcimon.screwbox.platformer.effects;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.ShaderBundle;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.BackgroundComponent;
import io.github.srcimon.screwbox.tiled.Layer;

import static io.github.srcimon.screwbox.core.graphics.options.SpriteDrawOptions.originalSize;

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
