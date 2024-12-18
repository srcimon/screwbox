package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;


/**
 * An {@link AssetBundle} for {@link Sound}s shipped with the {@link ScrewBox} game engine.
 */
public enum SoundBundle implements AssetBundle<Sound> {

    PHASER,
    JUMP,
    PLING,
    STEAM,
    ZISCH,
    NOTIFY,
    WATER,
    ARCHIVEMENT,
    SPLASH;

    private final Asset<Sound> sound;

    SoundBundle() {
        this.sound = Sound.assetFromFile("assets/sounds/%s.wav".formatted(this.name()));
    }

    @Override
    public Asset<Sound> asset() {
        return sound;
    }
}
