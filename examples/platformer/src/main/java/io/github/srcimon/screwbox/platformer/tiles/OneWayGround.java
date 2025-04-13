package io.github.srcimon.screwbox.platformer.tiles;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.physics.StaticColliderComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.tiles.Tile;

public class OneWayGround implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity().add(
                new RenderComponent(tile.sprite(), tile.layer().order()),
                new StaticColliderComponent(),
                new TransformComponent(tile.renderBounds()),
                new ColliderComponent(500, Percent.zero(), true));
    }

}
