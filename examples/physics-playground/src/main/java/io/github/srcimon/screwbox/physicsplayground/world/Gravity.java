package io.github.srcimon.screwbox.physicsplayground.world;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.tiled.Map;

public class Gravity implements SourceImport.Converter<Map> {

    @Override
    public Entity convert(Map map) {
        return new Entity().name("gravity")
                .add(new GravityComponent(Vector.y(600)));
    }
}