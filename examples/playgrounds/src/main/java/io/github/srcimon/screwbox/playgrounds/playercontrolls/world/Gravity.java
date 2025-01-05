package io.github.srcimon.screwbox.playgrounds.playercontrolls.world;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.core.utils.AsciiMap;

public class Gravity implements SourceImport.Converter<AsciiMap> {

    @Override
    public Entity convert(AsciiMap map) {
        return new Entity("gravity")
                .add(new GravityComponent(Vector.y(700)));
    }
}