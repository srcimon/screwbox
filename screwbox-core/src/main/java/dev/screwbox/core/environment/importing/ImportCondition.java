package dev.screwbox.core.environment.importing;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

//TODO add javadoc
public class ImportCondition<S, I> {

    private interface TriPredicate<S, I> {
        boolean test(S source, I index, ImportContext context);
    }

    private final TriPredicate<S, I> predicate;

    private ImportCondition(TriPredicate<S, I> predicate) {
        this.predicate = predicate;
    }

    @SafeVarargs
    public static <I, S> ImportCondition<S, I> allOf(ImportCondition<S, I>... criteria) {
        return new ImportCondition<>((s, i, c) -> Arrays.stream(criteria).allMatch(condition -> condition.predicate.test(s, i, c)));
    }

    public static <I, S> ImportCondition<S, I> always() {
        return new ImportCondition<>((s, i, c) -> true);
    }

    public static <S, I> ImportCondition<S, I> index(I index) {
        return new ImportCondition<>((s, i, c) -> i.equals(index));
    }

    public static <S, I> ImportCondition<S, I> probability(double p) {
        return new ImportCondition<>((s, i, c) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <S, I> ImportCondition<S, I> sourceMatches(Predicate<S> sourceCondition) {
        return new ImportCondition<>((s, i, c) -> sourceCondition.test(s));
    }

    public boolean matches(S source, I index, ImportContext context) {
        return predicate.test(source, index, context);
    }
}
