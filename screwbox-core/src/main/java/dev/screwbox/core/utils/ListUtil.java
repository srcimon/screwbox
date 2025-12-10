package dev.screwbox.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.Objects.isNull;

public class ListUtil {

    private static final Random RANDOM = new Random();

    private ListUtil() {
    }

    public static <T> List<T> emptyWhenNull(final List<T> list) {
        return isNull(list) ? new ArrayList<>() : list;
    }

    /**
     * Returns a new list containing all items from both input lists. Doesn't change the input lists.
     */
    public static <T> List<T> combine(final List<T> list, final List<T> other) {
        if (list.isEmpty()) {
            return other;
        }
        if (other.isEmpty()) {
            return list;
        }
        final List<T> all = new ArrayList<>(list);
        all.addAll(other);
        return all;
    }

    /**
     * Returns a new list containing all items from input list and the specified item.
     * Doesn't change the input list.
     *
     * @since 3.8.0
     */
    public static <T> List<T> combine(final List<T> list, final T item) {
        if (list.isEmpty()) {
            return List.of(item);
        }
        final List<T> all = new ArrayList<>(list);
        all.add(item);
        return all;
    }

    /**
     * Returns a new list containing all items from the specified input lists and ensures that only unique items
     * are returned.
     */
    public static <T> List<T> merge(final List<T> list, final List<T> other) {
        if (list.isEmpty()) {
            return other;
        }
        if (other.isEmpty()) {
            return list;
        }
        final Set<T> all = new HashSet<>(list);
        all.addAll(other);
        return new ArrayList<>(all);
    }

    /**
     * Returns a random item from the specified items.
     */
    @SafeVarargs
    public static <T> T randomFrom(final T... items) {
        return randomFrom(List.of(items));
    }

    /**
     * Returns a random item from the specified list.
     */
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
