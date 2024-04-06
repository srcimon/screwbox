package io.github.srcimon.screwbox.core.assets;

import java.util.function.Supplier;

/**
 * Marks enums (e.g. {@link FontsBundle}) to serve as container for {@link Assets}.
 * Marking non enums with this interface will result in {@link IllegalStateException}.
 * 
 * @see Assets#prepareClassPackage(Class) 
 */
public interface AssetBundle<T> extends Supplier<T> {

    /**
     * The {@link Asset} represented by each enum value.
     */
    Asset<T> asset();

    /**
     * Getter to get the {@link Asset} content directly.
     * @return
     */
    @Override
    default T get() {
        return asset().get();
    }
}
