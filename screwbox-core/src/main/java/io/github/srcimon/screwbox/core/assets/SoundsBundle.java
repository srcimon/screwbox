package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.audio.Sound;

//TODO javadoc and test
//TODO autoload all bundled assets on startup
public enum SoundsBundle implements AssetBundle<Sound> {

    PHASER(Asset.asset(() -> Sound.fromFile("assets/sounds/PHASER.wav")));

    private final Asset<Sound> sound;

    SoundsBundle(final Asset<Sound> sound) {
        this.sound = sound;
    }

    @Override
    public Asset<Sound> asset() {
        return sound;
    }
}
