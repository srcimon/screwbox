package dev.screwbox.platformer.props;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.AutoTileComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.DiggableComponent;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.tiled.Tile;

public class Diggable implements Converter<Tile> {

    @Override
    public Entity convert(final Tile tile) {
        return new Entity()
                .bounds(tile.renderBounds())
                .add(new AutoTileComponent(AutoTileBundle.BUBBLEGUM))
                .add(new RenderComponent(Sprite.invisible(), tile.layer().order()))
                .add(new DiggableComponent())
                .add(new ColliderComponent(500, Percent.zero()));
    }

}
