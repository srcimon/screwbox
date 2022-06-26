package de.suzufa.screwbox.core.audio;

import de.suzufa.screwbox.core.Percentage;

public interface Audio {

    Audio playEffect(Sound sound);

    Audio playEffect(SoundPool soundPool);

    Sound playEffectLooped(Sound sound);

    Sound playEffectLooped(SoundPool soundPool);

    Audio playMusic(Sound sound);

    Audio resume(Sound sound);

    Audio resumeLooped(Sound sound);

    Audio stop(Sound sound);

    Audio stopAllAudio();

    Audio setEffectVolume(Percentage volume);

    Audio setMusicVolume(Percentage volume);

}
