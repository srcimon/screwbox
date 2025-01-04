package io.github.srcimon.screwbox.playgrounds.playercontrolls.world;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.utils.AsciiMap;

public class Floor implements SourceImport.Converter<AsciiMap.Tile> {

    @Override
    public Entity convert(AsciiMap.Tile tile) {
        return new Entity("floor")
                .add(new TransformComponent(tile.bounds()))
                .add(new ColliderComponent(100));
    }
}
