package io.github.srcimon.screwbox.core.environment.audio;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

import java.util.Objects;

import static java.util.Objects.isNull;

//TODO add feature
public class SoundSystem implements EntitySystem {

    private static final Archetype SOUNDS = Archetype.of(SoundComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var soundEntity : engine.environment().fetchAll(SOUNDS)) {
            final var soundComponent = soundEntity.get(SoundComponent.class);
            if(isNull(soundComponent.playbackReference) || !engine.audio().isActive(soundComponent.playbackReference)) {
                soundComponent.playbackReference = engine.audio().playSound(soundComponent.sound, SoundOptions.playContinuously().position(soundEntity.position()));
            } else {
                engine.audio().updateOptions(soundComponent.playbackReference, SoundOptions.playContinuously().position(soundEntity.position()));
            }
        }
    }
}
