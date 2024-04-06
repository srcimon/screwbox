package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.audio.Sound;


/**
 * An {@link AssetBundle} for {@link Sound}s shipped with the {@link ScrewBox} game engine.
 */
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
