package dev.screwbox.core.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;

import static dev.screwbox.core.utils.ListUtil.containsDuplicates;

/**
 * Used for validating input values.
 */
public final class Validate {

    private Validate() {
    }

    /**
     * Value must be a positive int.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void positive(final int value, final String message) {
        if (value < 1) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Value must be a positive double.
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
    public static <T> void notEmpty(final Collection<T> list, final String message) {
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
     * Value must be below max value.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void max(final double value, final double max, final String message) {
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
     * Condition must be {@code true}.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void isTrue(final BooleanSupplier condition, final String message) {
        if (!condition.getAsBoolean()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Condition must be {@code false}.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void isFalse(final BooleanSupplier condition, final String message) {
        if (condition.getAsBoolean()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Value must be in range.
     *
     * @throws IllegalArgumentException using specified message when not
     * @since 2.15.0
     */
    public static void range(final int value, final int min, final int max, final String message) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Value must be in range.
     *
     * @throws IllegalArgumentException using specified message when not
     * @since 2.17.0
     */
    public static void range(final double value, final double min, final double max, final String message) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Value must be at least minimum.
     *
     * @throws IllegalArgumentException using specified message when not
     * @since 2.15.0
     */
    public static void min(final int value, final int min, final String message) {
        if (value < min) {
            throw new IllegalArgumentException(message);
        }
    }
}
