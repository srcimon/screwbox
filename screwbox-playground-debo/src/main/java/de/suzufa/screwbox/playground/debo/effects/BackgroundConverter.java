package de.suzufa.screwbox.playground.debo.effects;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.BatchImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.playground.debo.components.BackgroundComponent;
import de.suzufa.screwbox.tiled.Layer;

public class BackgroundConverter implements Converter<Layer> {

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
