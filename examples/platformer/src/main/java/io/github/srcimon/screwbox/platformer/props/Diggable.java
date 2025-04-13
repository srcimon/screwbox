package io.github.srcimon.screwbox.platformer.props;

import dev.screwbox.core.Percent;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.DiggableComponent;
import dev.screwbox.tiles.GameObject;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class Diggable implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/props/diggable.json");

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new RenderComponent(SPRITE.get().prepareShader(ShaderBundle.HURT), object.layer().order()),
                new DiggableComponent(),
                new TransformComponent(object.bounds()),
                new ColliderComponent(500, Percent.zero()));
    }

}
