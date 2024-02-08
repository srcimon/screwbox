package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;

import java.util.function.Supplier;

/**
 * Controls the audio playback of the {@link Engine}.
 */
public interface Audio {

    /**
     * Plays a {@link Sound} a single time with {@link AudioConfiguration#effectVolume()}.
     */
    Audio playEffect(Sound sound);

    /**
     * Plays a {@link Sound} from an {@link Asset} a single time with
     * {@link AudioConfiguration#effectVolume()}.
     */
    default Audio playEffect(final Supplier<Sound> sound) {
        return playEffect(sound.get());
    }

    /**
     * Plays a {@link Sound} looped with {@link AudioConfiguration#effectVolume()}. Can be stopped
     * with {@link #stop(Sound)}.
     */
    Audio playEffectLooped(Sound sound);

    /**
     * Plays a {@link Sound} looped with {@link AudioConfiguration#musicVolume()}. Can be stopped with
     * {@link #stop(Sound)}.
     */
    Audio playMusic(Sound sound);

    /**
     * Plays a {@link Sound} looped with {@link AudioConfiguration#musicVolume()} ()}. Can be stopped
     * with {@link #stop(Sound)}.
     */
    Audio playMusicLooped(Sound sound);

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