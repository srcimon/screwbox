package io.github.srcimon.screwbox.core.assets;

import java.util.function.Supplier;

public interface BundledAsset<T> extends Supplier<T> {

    Asset<T> asset();

    @Override
    default T get() {
        return asset().get();
    }
}
