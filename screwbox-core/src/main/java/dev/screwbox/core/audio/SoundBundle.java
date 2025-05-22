package dev.screwbox.core.audio;

import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.AssetBundle;


/**
 * An {@link AssetBundle} for {@link Sound}s shipped with the {@link ScrewBox} game engine.
 */
public enum SoundBundle implements AssetBundle<Sound> {

    PHASER,
    JUMP,
    PLING,
    STEAM,
    FLUID,
    ZISCH,
    NOTIFY,
    WATER,
    ACHIEVEMENT,
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
