package de.suzufa.screwbox.examples.platformer.props;

import static de.suzufa.screwbox.tiled.Tileset.spriteAssetFromJson;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.platformer.components.MovableComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class Box implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/props/box.json");

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new PhysicsBodyComponent(),
                new MovableComponent(),
                new TransformComponent(object.bounds()),
                new ColliderComponent(500, Percent.min()));
    }

}
