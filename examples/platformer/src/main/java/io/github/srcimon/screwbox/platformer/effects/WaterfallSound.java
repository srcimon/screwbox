package io.github.srcimon.screwbox.platformer.effects;

import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.audio.SoundComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class WaterfallSound implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id())
                .name("waterfall-sound")
                .add(new TransformComponent(object.bounds()))
                .add(new SoundComponent(Sound.fromFile("sounds/water.wav")));
    }
}