package dev.screwbox.core.environment.setup;

import dev.screwbox.core.utils.TileMap;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SetupCondition<T, I> {

    private BiPredicate<T, I> predicate;
    private ContextAwareBlueprint<T> blueprint;

    private SetupCondition(BiPredicate<T, I> predicate) {
        this.predicate = predicate;
    }

    @SafeVarargs
    public static <I, T> SetupCondition<T, I> allOf(SetupCondition<T, I>... criteria) {
        return new SetupCondition<>((s, i) -> Arrays.stream(criteria).allMatch(c -> c.predicate.test(s, i)));
    }
    public static <I, T> SetupCondition<T, I> always() {
        return new SetupCondition<>((s, i) -> true);
    }

    public static <T, I> SetupCondition<T, I> index(I index) {
        return new SetupCondition<>((s, i) -> i.equals(index));
    }

    public static <T, I> SetupCondition<T, I> probability(double p) {
        return new SetupCondition<>((s, i) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <T, I> SetupCondition<T, I> sourceMatches(Predicate<T> sourceCondition) {
        return new SetupCondition<>((s, i) -> sourceCondition.test(s));
    }

    public static SetupCondition<TileMap.Tile<Character>, Character> lastFailed() {
        //TODO context.lastMatchFailed();
        return null;
    }

    boolean matches(T source, I index) {//TODO import context
        return predicate.test(source, index);
    }
}
