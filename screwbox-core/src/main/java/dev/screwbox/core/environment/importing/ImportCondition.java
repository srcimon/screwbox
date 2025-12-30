package dev.screwbox.core.environment.importing;

import dev.screwbox.core.utils.Validate;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * A condition used to assign the source to a specific blueprint.
 *
 * @see <a href="https://screwbox.dev/docs/core-modules/environment#importing-level-data">Documentation</a>
 */
public class ImportCondition<S, I> implements BiPredicate<S, I> {

    private static final Random RANDOM = new Random();

    private final BiPredicate<S, I> predicate;

    private ImportCondition(final BiPredicate<S, I> predicate) {
        this.predicate = predicate;
    }

    /**
     * No specified condition must be met to return {@code true}.
     */
    @SafeVarargs
    public static <I, S> ImportCondition<S, I> noneOf(final ImportCondition<S, I>... criteria) {
        return new ImportCondition<>((s, i) -> Arrays.stream(criteria).noneMatch(condition -> condition.predicate.test(s, i)));
    }

    /**
     * Any specified condition must be met to return {@code true}.
     */
    @SafeVarargs
    public static <I, S> ImportCondition<S, I> anyOf(final ImportCondition<S, I>... criteria) {
        return new ImportCondition<>((s, i) -> Arrays.stream(criteria).anyMatch(condition -> condition.predicate.test(s, i)));
    }

    /**
     * All specified conditions must be met to return {@code true}.
     */
    @SafeVarargs
    public static <I, S> ImportCondition<S, I> allOf(final ImportCondition<S, I>... criteria) {
        return new ImportCondition<>((s, i) -> Arrays.stream(criteria).allMatch(condition -> condition.predicate.test(s, i)));
    }

    /**
     * Will be {@code true} for any input.
     */
    public static <I, S> ImportCondition<S, I> always() {
        return new ImportCondition<>((s, i) -> true);
    }

    /**
     * Will be {@code true} for input with the specified index.
     */
    public static <S, I> ImportCondition<S, I> index(I index) {
        return new ImportCondition<>((s, i) -> nonNull(i) && i.equals(index));
    }

    /**
     * Will be {@code true} for input with the specified probability (0-1).
     */
    public static <S, I> ImportCondition<S, I> probability(final double probability) {
        Validate.range(probability, 0, 1, "probability must be between 0 and 1");
        return new ImportCondition<>((s, i) -> RANDOM.nextDouble(0, 1) <= probability);
    }

    /**
     * Will be {@code true} for input sources that match the specified condition.
     */
    public static <S, I> ImportCondition<S, I> sourceMatches(final Predicate<S> sourceCondition) {
        return new ImportCondition<>((s, i) -> sourceCondition.test(s));
    }

    /**
     * Inverts the specified condition. Will be {@code true} if specified condition will return {@code false} and
     * vice versa.
     */
    public static <S, I> ImportCondition<S, I> not(final ImportCondition<S, I> condition) {
        return new ImportCondition<>((s, i) -> !condition.test(s, i));
    }

    /**
     * Will test the condition against the specified source and index.
     */
    @Override
    public boolean test(final S source, final I index) {
        return predicate.test(source, index);
    }
}