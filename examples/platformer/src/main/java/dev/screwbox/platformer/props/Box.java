package dev.screwbox.platformer.props;

import dev.screwbox.core.Percent;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.MovableComponent;
import dev.screwbox.tiled.GameObject;

import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

public class Box implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/props/box.json");

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new RenderComponent(SPRITE, object.layer().order()),
                new PhysicsComponent(),
                new MovableComponent(),
                new TransformComponent(object.bounds()),
                new ColliderComponent(500, Percent.zero()));
    }

}
