package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.audio.Sound;

//TODO javadoc and test
//TODO autoload all bundled assets on startup
public enum SoundsBundle implements AssetBundle<Sound> {

    PHASER,
    JUMP,
    PLING,
    STEAM,
    ZISCH;

    private final Asset<Sound> sound;

    SoundsBundle() {
        this.sound = Asset.asset(() -> Sound.fromFile("assets/sounds/%s.wav".formatted(this.name())));
    }

    @Override
    public Asset<Sound> asset() {
        return sound;
    }
}
