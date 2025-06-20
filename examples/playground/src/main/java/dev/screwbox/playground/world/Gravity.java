package dev.screwbox.playground.world;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.TileMap;

import static dev.screwbox.core.Vector.y;

public class Gravity implements SourceImport.Converter<TileMap<Color>> {

    @Override
    public Entity convert(TileMap<Color> object) {
        return new Entity()
                .name("gravity")
                .add(new GravityComponent(y(500)));
    }
}
