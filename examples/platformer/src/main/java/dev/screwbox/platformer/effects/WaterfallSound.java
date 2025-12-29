package dev.screwbox.platformer.effects;

import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.audio.SoundComponent;
import dev.screwbox.tiled.GameObject;

public class WaterfallSound implements Blueprint<GameObject> {

    @Override
    public Entity assembleFrom(GameObject object) {
        return new Entity(object.id())
                .name("waterfall-sound")
                .bounds(object.bounds())
                .add(new SoundComponent(SoundBundle.WATER));
    }
}