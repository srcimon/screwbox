package de.suzufa.screwbox.core.utils.internal;

public class Swappable<T> {

    private final T first;
    private final T second;
    private boolean swapped;

    public static <T> Swappable<T> of(final T first, final T second) {
        return new Swappable<>(first, second);
    }

    private Swappable(final T first, final T second) {
        this.first = first;
        this.second = second;
    }

    public T primary() {
        return swapped ? second : first;
    }

    public T backup() {
        return swapped ? first : second;
    }

    public void swap() {
        swapped = !swapped;
    }

}
