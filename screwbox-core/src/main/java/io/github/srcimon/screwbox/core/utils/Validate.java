package io.github.srcimon.screwbox.core.utils;

import java.util.List;

import static io.github.srcimon.screwbox.core.utils.ListUtil.containsDuplicates;

/**
 * Used for validating input values.
 */
public final class Validate {

    private Validate() {
    }

    /**
     * Value must be a postive int.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void positive(final int value, final String message) {
        if (value < 1) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Value must be a postive double.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void positive(final double value, final String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Value must zero or positive int.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void zeroOrPositive(final int value, final String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Value must zero or positive double.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void zeroOrPositive(final double value, final String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Value must not be an empty list.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static <T> void notEmpty(final List<T> list, final String message) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Value must not contain duplicate entries.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static <T> void noDuplicates(final List<T> list, final String message) {
        if (containsDuplicates(list)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Value must be below max value.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void max(final int value, final int max, final String message) {
        if (value > max) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Text must not be empty.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void notEmpty(final String title, final String message) {
        if (title.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Condition must be false.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void isFalse(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
