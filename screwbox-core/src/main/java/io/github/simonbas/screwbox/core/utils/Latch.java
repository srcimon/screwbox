package io.github.simonbas.screwbox.core.utils;

/**
 * Utility class that holds two instances of a class to be easily swapped
 * between an active and inactive state.
 */
public class Latch<T> {

    private T first;
    private T second;
    private boolean toggled;

    /**
     * New instance with the given variables.
     */
    public static <T> Latch<T> of(final T active, final T inactive) {
        return new Latch<>(active, inactive);
    }

    private Latch(final T first, final T second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the currently active one value.
     */
    public T active() {
        return toggled ? second : first;
    }

    /**
     * Returns the currently inactive value.
     */
    public T inactive() {
        return toggled ? first : second;
    }

    /**
     * Toggles between active and inactive value.
     */
    public void toggle() {
        toggled = !toggled;
    }

    /**
     * Overwrites the currently active value.
     */
    public void assignActive(final T primary) {
        if (toggled) {
            second = primary;
        } else {
            first = primary;
        }

    }

}
