package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;

import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.audio.SoundOptions.playOnce;

/**
 * Controls the audio playback of the {@link Engine}.
 */
public interface Audio {

    //TODO javadoc
    Audio playEffect(Sound sound, Vector position);

    //TODO Test and javadoc
    default Audio playEffect(final Supplier<Sound> sound, final Vector position) {
        return playEffect(sound.get(), position);
    }

    /**
     * Plays a {@link Sound} using the given {@link SoundOptions}.
     *
     * @see #playEffect(Supplier, SoundOptions)
     */
    Audio playEffect(Sound sound, SoundOptions options);

    /**
     * Plays a {@link Sound} using the given {@link SoundOptions}.
     *
     * @see #playEffect(Sound, SoundOptions)
     */
    default Audio playEffect(final Supplier<Sound> sound, final SoundOptions options) {
        return playEffect(sound.get(), options);
    }

    /**
     * Plays a {@link Sound} a single time with {@link AudioConfiguration#effectVolume()}.
     */
    default Audio playEffect(final Sound sound) {
        return playEffect(sound, playOnce());
    }

    /**
     * Plays a {@link Sound} from an {@link Asset} a single time with
     * {@link AudioConfiguration#effectVolume()}.
     */
    default Audio playEffect(final Supplier<Sound> sound) {
        return playEffect(sound.get());
    }

    /**
     * Plays a music {@link Sound} using the given {@link SoundOptions}.
     *
     * @see #playMusic(Supplier, SoundOptions)
     */
    Audio playMusic(Sound sound, SoundOptions options);

    /**
     * Plays a music {@link Sound} with {@link AudioConfiguration#musicVolume()}.
     */
    default Audio playMusic(Sound sound) {
        return playMusic(sound, playOnce());
    }

    /**
     * Plays a music {@link Sound}.
     *
     * @see #playMusic(Sound, SoundOptions)
     */
    default Audio playMusic(final Supplier<Sound> sound) {
        return playMusic(sound.get());
    }

    /**
     * Plays a music {@link Sound} using the given {@link SoundOptions}.
     *
     * @see #playMusic(Sound, SoundOptions)
     */
    default Audio playMusic(final Supplier<Sound> sound, final SoundOptions options) {
        return playMusic(sound.get(), options);
    }

    /**
     * Stops all currently playing instances of the {@link Sound}.
     */
    Audio stop(Sound sound);

    /**
     * Stops all currently playing {@link Sound}s.
     */
    Audio stopAllSounds();

    /**
     * Returns the count of currently playing instances of the given {@link Sound}.
     */
    int activeCount(Sound sound);

    /**
     * Returns {@code true} of there is any active playing instances of the given {@link Sound}.
     */
    boolean isActive(Sound sound);

    /**
     * Returns the count of currently playing {@link Sound}s.
     */
    int activeCount();

    /**
     * Read and change the current {@link AudioConfiguration}.
     */
    AudioConfiguration configuration();
}