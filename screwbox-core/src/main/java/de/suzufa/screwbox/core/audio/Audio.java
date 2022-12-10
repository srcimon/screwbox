package de.suzufa.screwbox.core.audio;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.assets.Asset;

/**
 * Controls the audio playback of the {@link Engine}.
 */
public interface Audio {

    /**
     * Plays a {@link Sound} a single time with {@link #effectVolume()}.
     */
    Audio playEffect(Sound sound);

    /**
     * Plays a {@link Sound} from an {@link Asset} a single time with
     * {@link #effectVolume()}.
     */
    default Audio playEffect(Asset<Sound> sound) {
        return playEffect(sound.get());
    }

    /**
     * Plays a {@link Sound} looped with {@link #effectVolume()}. Can be stopped
     * with {@link #stop(Sound)}.
     */
    Audio playEffectLooped(Sound sound);

    /**
     * Plays a {@link Sound} looped with {@link #musicVolume()}. Can be stopped with
     * {@link #stop(Sound)}.
     */
    Audio playMusic(Sound sound);

    /**
     * Stops all currently playing instances of the {@link Sound}.
     */
    Audio stop(Sound sound);

    /**
     * Stops all currently playing {@link Sound}s.
     */
    Audio stopAllSounds();

    /**
     * Sets the volume of all {@link Sound}s that are played via
     * {@link #playEffect(Sound)} and {@link #playEffectLooped(Sound)}.
     */
    Audio setEffectVolume(Percent volume);

    /**
     * Sets the volume of all {@link Sound}s that are played via
     * {@link #playMusic(Sound)}.
     */
    Audio setMusicVolume(Percent volume);

    /**
     * Returns the current volume for effects.
     * 
     * @see #musicVolume()
     */
    Percent effectVolume();

    /**
     * Returns the current volume for music.
     * 
     * @see #effectVolume()
     */
    Percent musicVolume();

    /**
     * Returns the count of currently playing instances of the given {@link Sound}.
     */
    int activeCount(Sound sound);

    /**
     * Returns the count of currently playing {@link Sound}s.
     */
    int activeCount();

    /**
     * Sets {@link #musicVolume()} to zero. Can be unmuted via
     * {@link #setMusicVolume(Percent)}.
     */
    Audio muteMusic();

    /**
     * Sets {@link #effectVolume()} to zero. Can be unmuted via
     * {@link #setEffectVolume(Percent)}.
     */
    Audio muteEffects();

}
