package dev.screwbox.core.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static dev.screwbox.core.utils.ListUtil.containsDuplicates;
import static java.util.Objects.nonNull;

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
            validationException(message, value);
        }
    }

    /**
     * Value must be a positive double.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void positive(final double value, final String message) {
        if (value <= 0) {
            validationException(message, value);
        }
    }

    /**
     * Value must zero or positive int.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void zeroOrPositive(final int value, final String message) {
        if (value < 0) {
            validationException(message, value);
        }
    }

    /**
     * Value must zero or positive double.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void zeroOrPositive(final double value, final String message) {
        if (value < 0) {
            validationException(message, value);
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
            validationException(message, value);
        }
    }

    /**
     * Value must be below max value.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void max(final double value, final double max, final String message) {
        if (value > max) {
            validationException(message, value);
        }
    }

    /**
     * Text must not be empty.
     *
     * @throws IllegalArgumentException using specified message when not
     */
    public static void notEmpty(final String value, final String message) {
        if (value.isEmpty()) {
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
            validationException(message, value);
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
            validationException(message, value);
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
            validationException(message, value);
        }
    }

    /**
     * Value must not be null.
     *
     * @throws IllegalArgumentException using specified message when not
     * @since 3.8.0
     */
    public static void isNull(final Supplier<Object> value, final String message) {
        if (nonNull(value.get())) {
            throw new IllegalArgumentException(message);
        }
    }

    private static <T> void validationException(final String message, final T value) {
        throw new IllegalArgumentException("%s (actual value: %s)".formatted(message, value));
    }

}
