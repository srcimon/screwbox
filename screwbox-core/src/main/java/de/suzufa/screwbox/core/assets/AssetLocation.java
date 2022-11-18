package de.suzufa.screwbox.core.assets;

import java.lang.reflect.Field;

public record AssetLocation<T> (Asset<T> asset, Class<?> sourceClass, Field sourceField) {
}
