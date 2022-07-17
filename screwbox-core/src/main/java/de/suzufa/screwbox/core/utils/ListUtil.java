package de.suzufa.screwbox.core.utils;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ListUtil {

    private static final Random RANDOM = new Random();

    private ListUtil() {
    }

    public static <T> List<T> emptyWhenNull(final List<T> list) {
        return isNull(list) ? new ArrayList<>() : list;
    }

    public static <T> List<T> merge(final List<T> list, final List<T> other) {
        if (list.isEmpty()) {
            return other;
        }
        if (other.isEmpty()) {
            return list;
        }
        final HashSet<T> all = new HashSet<>(list);
        all.addAll(other);
        return new ArrayList<>(all);
    }

    @SafeVarargs
    public static <T> T randomFrom(final T... items) {
        return randomFrom(List.of(items));
    }

    public static <T> T randomFrom(final List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        final var index = RANDOM.nextInt(0, list.size());
        return list.get(index);
    }

}
