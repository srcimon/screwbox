package de.suzufa.screwbox.core.audio;

import de.suzufa.screwbox.core.Percentage;

public interface Audio {

    Audio playEffect(Sound sound);

    Sound playEffectLooped(Sound sound);

    Audio playMusic(Sound sound);

    Audio resume(Sound sound);

    Audio resumeLooped(Sound sound);

    Audio stop(Sound sound);

    Audio stopAllAudio();

    Audio setEffectVolume(Percentage volume);

    Audio setMusicVolume(Percentage volume);

}
