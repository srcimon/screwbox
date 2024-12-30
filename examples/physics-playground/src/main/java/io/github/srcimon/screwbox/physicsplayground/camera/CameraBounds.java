package io.github.srcimon.screwbox.physicsplayground.camera;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.tiled.Map;

public class CameraBounds implements SourceImport.Converter<Map> {

    @Override
    public Entity convert(Map map) {
        return new Entity().name("camera-bounds")
                .add(new CameraBoundsComponent(map.bounds()));
    }
}
