package de.suzufa.screwbox.core.utils;

public class Lever<T> {

    private final T first;
    private final T second;
    private boolean swapped;

    public static <T> Lever<T> of(final T first, final T second) {
        return new Lever<>(first, second);
    }

    private Lever(final T first, final T second) {
        this.first = first;
        this.second = second;
    }

    public T primary() {
        return swapped ? second : first;
    }

    public T backup() {
        return swapped ? first : second;
    }

    public void gearNext() {
        swapped = !swapped;
    }

}
