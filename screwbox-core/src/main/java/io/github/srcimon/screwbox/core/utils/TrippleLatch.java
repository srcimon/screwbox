package io.github.srcimon.screwbox.core.utils;

import java.util.List;

public class TrippleLatch<T> {

    private final List<T> values;
    private int index = 0;

    public static <T> TrippleLatch<T> of(final T first, final T second, final T third) {
        return new TrippleLatch<>(List.of(first, second, third));
    }

    private TrippleLatch(final List<T> values) {
        this.values = values;
    }

    public T active() {
        return values.get(index);
    }

    public T inactive() {
        return values.get(increase(1));
    }

    public T backupInactive() {
        return values.get(increase(2));
    }

    private int increase(final int change) {
        final int value = index + change;
        return value > 2 ? value - 3 : value;
    }

    public void toggle() {
        index = index == 0 ? 2 : index - 1;
    }

}
