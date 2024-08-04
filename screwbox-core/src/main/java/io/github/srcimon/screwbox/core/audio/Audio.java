package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.Asset;

import java.util.List;
import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.audio.SoundOptions.playOnce;

/**
 * Controls the audio playback of the {@link Engine}.
 */
public interface Audio {

    /**
     * Returns the current microphone input volume. Microphone will turn on automatically when calling this method. Will turn off after
     * {@link AudioConfiguration#microphoneIdleTimeout()}
     */
    Percent microphoneLevel();

    /**
     * Returns {@code true} if the the microphone is currently active.
     */
    boolean isMicrophoneActive();

    List<Playback> activePlaybacks();

    /**
     * Plays a {@link Sound} using the given {@link SoundOptions}.
     *
     * @see #playSound(Supplier, SoundOptions)
     */
    //TODO document playback reference
    Playback playSound(Sound sound, SoundOptions options);

    Audio stopPlayback(Playback playback);
    /**
     * Plays a {@link Sound} using the given {@link SoundOptions}.
     *
     * @see #playSound(Sound, SoundOptions)
     */
    default Playback playSound(final Supplier<Sound> sound, final SoundOptions options) {
        return playSound(sound.get(), options);
    }

    /**
     * Plays a {@link Sound} a single time.
     */
    default Playback playSound(final Sound sound) {
        return playSound(sound, playOnce());
    }

    /**
     * Plays a {@link Sound} from an {@link Asset} a single time.
     */
    default Playback playSound(final Supplier<Sound> sound) {
        return playSound(sound.get());
    }

    boolean isAlive(Playback playback);

    boolean updateOptions(Playback playback, SoundOptions options);

    /**
     * Stops all currently playing instances of the {@link Sound}.
     *
     * @see #stopSound(Supplier)
     */
    Audio stopSound(Sound sound);

    /**
     * Stops all currently playing instances of the {@link Sound} that is provied by the {@link Supplier}.
     *
     * @see #stopSound(Sound)
     */
    default Audio stopSound(Supplier<Sound> sound) {
        return stopSound(sound.get());
    }

    /**
     * Stops all currently playing {@link Sound}s.
     */
    Audio stopAllSounds();

    /**
     * Returns the count of currently playing instances of the given {@link Sound}.
     */
    int activeCount(Sound sound);

    /**
     * Returns the count of currently playing instances of the {@link Sound} given by the {@link Supplier}.
     */
    default int activeCount(Supplier<Sound> sound) {
        return activeCount(sound.get());
    }

    /**
     * Returns {@code true} of there is any active playing instances of the given {@link Sound}.
     */
    boolean isAlive(Sound sound);

    /**
     * Returns {@code true} of there is any active playing instances of the {@link Sound} given by the {@link Supplier}.
     */
    default boolean isAlive(Supplier<Sound> sound) {
        return isAlive(sound.get());
    }

    /**
     * Returns the count of currently playing {@link Sound}s.
     */
    int activeCount();

    /**
     * Read and change the current {@link AudioConfiguration}.
     */
    AudioConfiguration configuration();
}