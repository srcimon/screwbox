package dev.screwbox.core.graphics;

import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.AssetBundle;

import java.util.function.Supplier;

/**
 * An {@link AssetBundle} for {@link LensFlare lens flares} shipped with the {@link ScrewBox} game engine.
 *
 * @since 3.8.0
 */
public enum LensFlareBundle implements AssetBundle<LensFlare> {

    SHY(() -> LensFlare.noRays()
            .orb(-1.3, 0.5, 0.08)
            .orb(2.3, 0.3, 0.12)
            .orb(1.4, 0.2, 0.08)),
    LITTLE(() -> LensFlare.noRays()
            .orb(1.3, 0.5, 0.125)
            .orb(2.4, 0.2, 0.185)
            .orb(-1.5, 1.0, 0.125)),
    BEAM(() -> LensFlare.rayCount(2)
            .rayWidth(6)
            .rayOpacity(0.1)
            .rayLength(1.25));

    private final Asset<LensFlare> asset;

    LensFlareBundle(final Supplier<LensFlare> asset) {
        this.asset = Asset.asset(asset);
    }

    @Override
    public Asset<LensFlare> asset() {
        return asset;
    }
}
