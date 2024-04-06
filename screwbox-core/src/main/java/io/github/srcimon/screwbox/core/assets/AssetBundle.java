package io.github.srcimon.screwbox.core.assets;

import java.util.function.Supplier;
//TODO RENAME  FontsBundle, SpritesBundle, SoundsBundle
public interface AssetBundle<T> extends Supplier<T> {

    Asset<T> asset();

    @Override
    default T get() {
        return asset().get();
    }
}
