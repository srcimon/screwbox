package de.suzufa.screwbox.core.audio;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;

/**
 * Controls the audio playback of the {@link Engine}.
 */
public interface Audio {

    /**
     * Plays a {@link Sound} a single time with {@link #effectVolume()}.
     */
    Audio playEffect(Sound sound);

    /**
     * Plays a {@link Sound} looped with {@link #effectVolume()}. Can be stopped
     * with {@link #stop(Sound)}.
     */
    Audio playEffectLooped(Sound sound);

    Audio playMusic(Sound sound);

    Audio resume(Sound sound);

    Audio resumeLooped(Sound sound);

    Audio stop(Sound sound);

    Audio stopAllSounds();

    Audio setEffectVolume(Percentage volume);

    Audio setMusicVolume(Percentage volume);

    Percentage effectVolume();

    Percentage musicVolume();

    int activeCount(Sound sound);

    int activeCount();

}
