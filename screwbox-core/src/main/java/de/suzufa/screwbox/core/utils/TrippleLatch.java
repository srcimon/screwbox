package de.suzufa.screwbox.core.utils;

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

    public T primary() {
        return values.get(index);
    }

    public T backup() {
        return values.get(higherIndex(1));
    }

    public T secondaryBackup() {
        return values.get(higherIndex(2));
    }

    // TODO: optimize
    private int higherIndex(int change) {
        int value = index + change;
        if (value == 3) {
            return 0;
        }
        if (value == 4) {
            return 1;
        }
        return value;
    }

    public void swap() {
        index = index == 0 ? 2 : index - 1;
    }

}
