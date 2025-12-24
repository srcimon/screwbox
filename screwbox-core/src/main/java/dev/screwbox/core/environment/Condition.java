package dev.screwbox.core.environment;

import dev.screwbox.core.utils.TileMap;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Condition<T, I> {

    private BiPredicate<T, I> predicate;

    private Condition(BiPredicate<T, I> predicate) {
        this.predicate = predicate;
    }

    @SafeVarargs
    public static <I, T> Condition<T, I> allOf(Condition<T, I>... criteria) {
        return new Condition<>((s, i) -> Arrays.stream(criteria).allMatch(c -> c.predicate.test(s, i)));
    }
    public static <I, T> Condition<T, I> always() {
        return new Condition<>((s, i) -> true);
    }

    public static <T, I> Condition<T, I> index(I index) {
        return new Condition<>((s, i) -> i.equals(index));
    }

    public static <T, I> Condition<T, I> probability(double p) {
        return new Condition<>((s, i) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <T, I> Condition<T, I> sourceMatches(Predicate<T> sourceCondition) {
        return new Condition<>((s, i) -> sourceCondition.test(s));
    }

    public static Condition<TileMap.Tile<Character>, Character> allA() {
        //TODO context.lastMatchFailed();
        return null;
    }

    boolean matches(T source, I index) {//TODO import context
        return predicate.test(source, index);
    }
}
