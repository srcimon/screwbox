package de.suzufa.screwbox.core.assets;

import java.util.function.Supplier;

//TODO: javadoc and test
public class Asset<T> {

    private final Supplier<T> supplier;
    private T value;

    public void load() {
        value = supplier.get();
        if (value == null) {
            throw new IllegalStateException("asset null after loading");
        }
    }

    public static <T> Asset<T> asset(Supplier<T> supplier) {
        // TODO Auto-generated method stub
        return new Asset<T>(supplier);
    }

    private Asset(final Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        // TODO: throw exception when not filled?
        if (value != null) {
            return value;
        }
        load();
        return value;
    }
}
