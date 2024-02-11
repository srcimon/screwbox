package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;

record ActiveSound(Sound sound, SoundOptions options, boolean isMusic) {

    boolean isEffect() {
        return !isMusic;
    }
}
