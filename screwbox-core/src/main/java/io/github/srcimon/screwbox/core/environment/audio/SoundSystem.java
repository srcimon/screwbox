package io.github.srcimon.screwbox.core.environment.audio;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Adds a continuously playing {@link Sound} to an {@link Entity} linked to it's position.
 */
public class SoundSystem implements EntitySystem {

    private static final Archetype SOUNDS = Archetype.ofSpacial(SoundComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(SOUNDS)) {
            final var soundComponent = entity.get(SoundComponent.class);

            // twice the distance on purpose to not cause flickering on edge
            final double enableRange = engine.audio().configuration().soundRange() * 2;

            final boolean isInRange = engine.graphics().isWithinDistanceToVisibleArea(entity.position(), enableRange);

            if (isInRange) {
                final SoundOptions soundOptions = SoundOptions.playOnce().position(entity.position());
                if (isNull(soundComponent.playback) || !engine.audio().playbackIsActive(soundComponent.playback)) {
                    soundComponent.playback = engine.audio().playSound(soundComponent.sound, soundOptions);
                } else {
                    engine.audio().updatePlaybackOptions(soundComponent.playback, soundOptions);
                }
            } else if (nonNull(soundComponent.playback)) {
                engine.audio().stopPlayback(soundComponent.playback);
                soundComponent.playback = null;
            }
        }
    }

}
