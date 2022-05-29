package de.suzufa.screwbox.core.utils;

import java.util.List;

public class TrippleLatch<T> {

    private final List<T> values;
    private int index = 2;

    public static <T> TrippleLatch<T> of(final T first, final T second, final T third) {
        return new TrippleLatch<>(List.of(third, second, first));
    }

    private TrippleLatch(final List<T> values) {
        this.values = values;
    }

    public T primary() {
        return values.get(index);
    }

    public T backup() {
        return values.get(lowerIndex(1));
    }

    public T secondaryBackup() {
        return values.get(lowerIndex(2));
    }

    // TODO: optimize
    private int lowerIndex(int change) {
        int value = index - change;
        if (value == -2) {
            return 2;
        }
        if (value == -1) {
            return 1;
        }
        return value;
    }

    // TODO: optimize
    public void swap() {
        index++;
        if (index > 2) {
            index = 0;
        }
    }

}
