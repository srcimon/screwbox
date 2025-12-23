package dev.screwbox.core.environment.importing;

import dev.screwbox.core.utils.TileMap;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class By<T, I> {

    private BiPredicate<T, I> predicate;
    private ContextAwareBlueprint<T> blueprint;

    private By(BiPredicate<T, I> predicate) {
        this.predicate = predicate;
    }

    @SafeVarargs
    public static <I, T> By<T, I> allOf(By<T, I>... criteria) {
        return new By<>((s, i) -> Arrays.stream(criteria).allMatch(c -> c.predicate.test(s, i)));
    }
    public static <I, T> By<T, I> always() {
        return new By<>((s, i) -> true);
    }

    public static <T, I> By<T, I> index(I index) {
        return new By<>((s, i) -> i.equals(index));
    }

    public static <T, I> By<T, I> probability(double p) {
        return new By<>((s, i) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <T, I> By<T, I> sourceMatches(Predicate<T> sourceCondition) {
        return new By<>((s, i) -> sourceCondition.test(s));
    }

    public static By<TileMap.Tile<Character>, Character> lastFailed() {
        //TODO context.lastMatchFailed();
        return null;
    }

    boolean matches(T source, I index) {//TODO import context
        return predicate.test(source, index);
    }
}
