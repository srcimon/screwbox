package dev.screwbox.playground.world;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.utils.AsciiMap;

import static dev.screwbox.core.Vector.y;

public class Gravity implements SourceImport.Converter<AsciiMap> {

    @Override
    public Entity convert(AsciiMap object) {
        return new Entity()
                .name("gravity")
                .add(new GravityComponent(y(500)));
    }
}
