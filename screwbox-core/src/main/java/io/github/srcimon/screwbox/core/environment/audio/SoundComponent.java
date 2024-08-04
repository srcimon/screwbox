package io.github.srcimon.screwbox.core.environment.audio;

import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.graphics.Camera;

import java.io.Serial;
import java.util.function.Supplier;

/**
 * Adds a constantly playing sound output to an {@link Entity}. The {@link SoundOptions#volume()} and {@link SoundOptions#pan()}
 * will be automatically adjusted by the distance and direction of the {@link Entity} to the {@link Camera}.
 */
public class SoundComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Sound sound;
    public Playback playback;

    public SoundComponent(final Supplier<Sound> sound) {
        this(sound.get());
    }

    public SoundComponent(final Sound sound) {
        this.sound = sound;
    }
}
