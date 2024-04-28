package io.github.srcimon.screwbox.core.assets;

import java.util.function.Supplier;

/**
 * Marks enums ({@link FontBundle}) to serve as container for {@link Assets}.
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
     */
    @Override
    default T get() {
        return asset().get();
    }
}
