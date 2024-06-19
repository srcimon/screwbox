package io.github.srcimon.screwbox.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static java.util.Objects.isNull;

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

    /**
     * Returns {@code true} if the given {@link List} contains duplicate entries.
     */
    public static <T> boolean containsDuplicates(final List<T> list) {
        final List<T> uniques = new ArrayList<>();

        for (final T value : list) {
            if (!uniques.contains(value)) {
                uniques.add(value);
            }
        }
        return uniques.size() != list.size();
    }

}
