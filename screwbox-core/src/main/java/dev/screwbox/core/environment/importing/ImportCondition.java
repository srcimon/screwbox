package dev.screwbox.core.environment.importing;

import dev.screwbox.core.utils.Validate;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

/**
 * A condition used to assign the source to a specific blueprint.
 *
 * @see <a href="https://screwbox.dev/docs/core-modules/environment#importing-level-data">Documentation</a>
 */
public class ImportCondition<S, I> {

    private static final Random RANDOM = new Random();

    private interface TriPredicate<S, I> {
        boolean test(S source, I index, ImportContext context);
    }

    private final TriPredicate<S, I> predicate;

    private ImportCondition(final TriPredicate<S, I> predicate) {
        this.predicate = predicate;
    }

    //TODO anyOf

    /**
     * All specified conditions must be met to return {@code true}.
     */
    @SafeVarargs
    public static <I, S> ImportCondition<S, I> allOf(final ImportCondition<S, I>... criteria) {
        return new ImportCondition<>((s, i, c) -> Arrays.stream(criteria).allMatch(condition -> condition.predicate.test(s, i, c)));
    }

    /**
     * Will be {@code true} for any input.
     */
    public static <I, S> ImportCondition<S, I> always() {
        return new ImportCondition<>((s, i, c) -> true);
    }

    /**
     * Will be {@code true} for input with the specified index.
     */
    public static <S, I> ImportCondition<S, I> index(I index) {
        return new ImportCondition<>((s, i, c) -> i.equals(index));
    }

    /**
     * Will be {@code true} for input with the specified probability (0-1).
     */
    public static <S, I> ImportCondition<S, I> probability(final double probability) {
        Validate.range(probability, 0, 1, "probability must be between 0 and 1");//TODO test
        return new ImportCondition<>((s, i, c) -> RANDOM.nextDouble(0, probability) < 1);//TODO quark
    }

    /**
     * Will be {@code true} for input sources that match the specified condition.
     */
    public static <S, I> ImportCondition<S, I> sourceMatches(final Predicate<S> sourceCondition) {
        return new ImportCondition<>((s, i, c) -> sourceCondition.test(s));
    }

    /**
     * Will be {@code true} if the last assignment did not create any entities.
     */
    public static <S, I> ImportCondition<S, I> lastAssignmentFailed() {
        return new ImportCondition<>((s, i, c) -> c.previousEntityCount() == 0);
    }

    /**
     * Inverts the specified condition. Will be {@code true} if specified condition will return {@code false} and
     * vice versa.
     */
    public static <S, I> ImportCondition<S, I> not(final ImportCondition<S, I> condition) {
        return new ImportCondition<>((s, i, c) -> !condition.test(s, i, c));
    }

    /**
     * Will test the condition against the specified source, index and import context.
     */
    public boolean test(final S source, final I index, final ImportContext context) {
        return predicate.test(source, index, context);
    }
}