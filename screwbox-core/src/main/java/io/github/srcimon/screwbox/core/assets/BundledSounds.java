package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.audio.Sound;

//TODO javadoc and test
//TODO REMOVE Sound.dummy()
//TODO autoload all bundled assets on startup
public enum BundledSounds implements BundledAsset<Sound> {

    PHASER(Asset.asset(() -> Sound.fromFile("assets/sounds/dummy_effect.wav"))); //TODO: RENAME FILE

    private final Asset<Sound> sound;

    BundledSounds(final Asset<Sound> sound) {
        this.sound = sound;
    }

    @Override
    public Asset<Sound> asset() {
        return sound;
    }
}
