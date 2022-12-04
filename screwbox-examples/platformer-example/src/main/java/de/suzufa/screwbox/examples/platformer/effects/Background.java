package de.suzufa.screwbox.examples.platformer.effects;

import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.platformer.components.BackgroundComponent;
import de.suzufa.screwbox.tiled.Layer;

public class Background implements Converter<Layer> {

    @Override
    public Entity convert(Layer layer) {
        String imagePath = layer.image().orElseThrow().replace("../", "");
        Sprite image = Sprite.fromFile(imagePath);
        var backgroundComponent = new BackgroundComponent(
                layer.parallaxX(), layer.parallaxY(),
                layer.properties().forceDouble("zoom"));

        var spriteComponent = new SpriteComponent(image, layer.order(), layer.opacity());
        return new Entity().add(backgroundComponent, spriteComponent);
    }

}
