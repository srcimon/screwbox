package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.audio.Sound;

import java.util.function.Supplier;

public enum BundledSounds implements Supplier<Sound> {

    PHASER(() -> Sound.fromFile("assets/sounds/dummy_effect.wav")) //TODO: RENAME FILE ; ;

    private final Supplier<Sound> sound;

    BundledSounds(final Supplier<Sound> sound) {
        this.sound = sound;
    }

    @Override
    public Sound get() {
        return sound.get();
    }

}
