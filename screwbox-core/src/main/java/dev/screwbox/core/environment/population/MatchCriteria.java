package dev.screwbox.core.environment.population;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MatchCriteria<T, I> {

    private BiPredicate<T, I> predicate;

    private MatchCriteria(BiPredicate<T, I> predicate) {
        this.predicate = predicate;
    }

    public static <T, I> MatchCriteria<T, I> onIndexValue(I index) {
        return new MatchCriteria<>((s, i) -> i.equals(index));
    }

    public static <T, I> MatchCriteria<T, I> onProbability(double p) {
        return new MatchCriteria<>((s, i) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <T, I> MatchCriteria<T, I> onCondition(Predicate<T> sourceCondition) {
        return new MatchCriteria<>((s, i) -> sourceCondition.test(s));
    }

    public boolean matches(T source, I index) {//TODO import context
        return predicate.test(source, index);
    }
}
