package io.github.simonbas.screwbox.examples.platformer.props;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.ColliderComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.examples.platformer.components.DiggableComponent;
import io.github.simonbas.screwbox.tiled.GameObject;

import static io.github.simonbas.screwbox.tiled.Tileset.spriteAssetFromJson;

public class Diggable implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/props/diggable.json");

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new DiggableComponent(),
                new TransformComponent(object.bounds()),
                new ColliderComponent(500, Percent.min()));
    }

}
