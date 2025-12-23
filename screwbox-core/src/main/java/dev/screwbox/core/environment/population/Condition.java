package dev.screwbox.core.environment.population;

import dev.screwbox.core.utils.TileMap;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Condition<T, I> {

    private BiPredicate<T, I> predicate;

    private Condition(BiPredicate<T, I> predicate) {
        this.predicate = predicate;
    }

    public static <T, I> Condition<T, I> index(I index) {
        return new Condition<>((s, i) -> i.equals(index));
    }

    public static <T, I> Condition<T, I> probability(double p) {
        return new Condition<>((s, i) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <T, I> Condition<T, I> sourceCondition(Predicate<T> sourceCondition) {
        return new Condition<>((s, i) -> sourceCondition.test(s));
    }

    public boolean matches(T source, I index) {//TODO import context
        return predicate.test(source, index);
    }
}
