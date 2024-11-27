package io.github.srcimon.screwbox.platformer.effects;

import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.audio.SoundComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class WaterfallSound implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id())
                .name("waterfall-sound")
                .bounds(object.bounds())
                .add(new SoundComponent(SoundBundle.WATER));
    }
}