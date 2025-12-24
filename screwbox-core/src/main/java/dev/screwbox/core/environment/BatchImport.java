package dev.screwbox.core.environment;

public class BatchImport<T, I> {

    public BatchImport<T, I> test(T t) {
        return this;
    }

    public BatchImport<T, I> assign(Condition<T, I> condition, Blueprint<T> blueprint) {
        return this;
    }

    public BatchImport<T, I> verify(Condition<T, I> condition, String message) {
        return this;
    }

    public BatchImport<T, I> assign(I t, Blueprint<T> blueprint) {
        return this;
    }
}
