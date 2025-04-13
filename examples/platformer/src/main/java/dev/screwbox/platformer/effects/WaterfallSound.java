package dev.screwbox.platformer.effects;

import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.audio.SoundComponent;
import dev.screwbox.tiles.GameObject;

public class WaterfallSound implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id())
                .name("waterfall-sound")
                .bounds(object.bounds())
                .add(new SoundComponent(SoundBundle.WATER));
    }
}