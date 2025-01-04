package io.github.srcimon.screwbox.playgrounds.playercontrolls.world;

import io.github.srcimon.screwbox.core.environment.AsciiTile;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;

public class Floor implements SourceImport.Converter<AsciiTile> {

    @Override
    public Entity convert(AsciiTile tile) {
        return new Entity("floor")
                .add(new TransformComponent(tile.position(), tile.size(), tile.size()))
                .add(new ColliderComponent());
    }
}
