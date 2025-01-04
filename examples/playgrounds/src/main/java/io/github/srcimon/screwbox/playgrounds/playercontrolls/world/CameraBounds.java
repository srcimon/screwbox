package io.github.srcimon.screwbox.playgrounds.playercontrolls.world;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.core.utils.AsciiMap;

public class CameraBounds implements SourceImport.Converter<AsciiMap> {

    @Override
    public Entity convert(AsciiMap map) {
        return new Entity("camera-bounds")
                .add(new CameraBoundsComponent(map.bounds()));
    }
}
