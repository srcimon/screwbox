package dev.screwbox.core.environment.population;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class PopulationMatch<T, I> {

    private BiPredicate<T, I> predicate;

    private PopulationMatch(BiPredicate<T, I> predicate) {
        this.predicate = predicate;
    }

    public static <T, I> PopulationMatch<T, I> indexIs(I index) {
        return new PopulationMatch<>((s, i) -> i.equals(index));
    }

    public static <T, I> PopulationMatch<T, I> hasPropability(double p) {
        return new PopulationMatch<>((s, i) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <T, I> PopulationMatch<T, I> meetsCondition(Predicate<T> sourceCondition) {
        return new PopulationMatch<>((s, i) -> sourceCondition.test(s));
    }

    public boolean matches(T source, I index) {//TODO import context
        return predicate.test(source, index);
    }
}
