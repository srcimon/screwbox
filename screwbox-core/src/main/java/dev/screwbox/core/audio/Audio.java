package dev.screwbox.core.audio;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.assets.Asset;

import java.util.List;
import java.util.function.Supplier;

import static dev.screwbox.core.audio.SoundOptions.playOnce;

/**
 * Controls the audio playback of the {@link Engine}.
 *
 * @see <a href="http://screwbox.dev/docs/core-modules/audio">Documentation</a>
 */
public interface Audio {

    /**
     * Returns the count of currently started audio lines. Lines will be automatically created when needed. To reduce
     * system load use a common audio format for all your sounds, because an audio line can only play one specific format.
     *
     * @see AudioConfiguration#maxLines()
     */
    int lineCount();

    /**
     * Returns the current microphone input volume. Microphone will turn on automatically when calling this method. Will turn off after
     * {@link AudioConfiguration#microphoneIdleTimeout()}
     */
    Percent microphoneLevel();

    /**
     * Returns {@code true} if the the microphone is currently active.
     */
    boolean isMicrophoneActive();

    /**
     * Returns a list of all currently active {@link Playback playbacks}.
     */
    List<Playback> activePlaybacks();

    /**
     * Plays a {@link Sound} using the given {@link SoundOptions}. Returns a reference to the {@link Playback} created.
     *
     * @see #playSound(Supplier, SoundOptions)
     */
    Playback playSound(Sound sound, SoundOptions options);

    /**
     * Stops a single {@link Playback}. If the {@link Playback} has already ended won't do anything.
     */
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

    /**
     * Returns {@code true} if the specified {@link Playback} is active.
     */
    boolean isPlaybackActive(Playback playback);

    /**
     * Changes the {@link SoundOptions} of the specified {@link Playback}. Will return {@code true} if {@link Playback}
     * is still active and could be changed. Otherwise return value will be {@code false}. Be aware that the speed of an
     * audio playback cannot be changed afterwards.
     */
    boolean updatePlaybackOptions(Playback playback, SoundOptions options);

    /**
     * Stops all current {@link Playback playbacks} of the specified {@link Sound}.
     *
     * @see #stopAllPlaybacks(Supplier)
     */
    Audio stopAllPlaybacks(Sound sound);

    /**
     * Stops all currently playing instances of the {@link Sound} that is provided by the {@link Supplier}.
     *
     * @see #stopAllPlaybacks(Sound)
     */
    default Audio stopAllPlaybacks(Supplier<Sound> sound) {
        return stopAllPlaybacks(sound.get());
    }

    /**
     * Stops all currently {@link #activePlaybacks()}.
     */
    Audio stopAllPlaybacks();

    /**
     * Returns the total number of finished playbacks. Playing a {@link Sound} multiple times within one {@link Playback}
     * counts as one.
     *
     * @see #soundsPlayedCount()
     */
    int completedPlaybackCount();

    /**
     * Returns the total number of finished sounds. Playing a {@link Sound} multiple times within one {@link Playback}
     * counts multiple times.
     *
     * @see #completedPlaybackCount()
     */
    int soundsPlayedCount();

    /**
     * Returns the count of currently playing instances of the given {@link Sound}.
     */
    int activePlaybackCount(Sound sound);

    /**
     * Returns the count of currently playing instances of the {@link Sound} given by the {@link Supplier}.
     */
    default int activePlaybackCount(Supplier<Sound> sound) {
        return activePlaybackCount(sound.get());
    }

    /**
     * Returns {@code true} of there is any active playing instances of the given {@link Sound}.
     */
    boolean hasActivePlaybacks(Sound sound);

    /**
     * Returns {@code true} of there is any active playing instances of the {@link Sound} given by the {@link Supplier}.
     */
    default boolean hasActivePlaybacks(Supplier<Sound> sound) {
        return hasActivePlaybacks(sound.get());
    }

    /**
     * Returns the count of currently active {@link Playback playbacks}.
     */
    int activePlaybackCount();

    /**
     * Read and change the current {@link AudioConfiguration}.
     */
    AudioConfiguration configuration();

}