package dev.screwbox.core.environment.bulkimport;

import dev.screwbox.core.utils.TileMap;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ImportCondition<T, I> {

    private BiPredicate<T, I> predicate;

    private ImportCondition(BiPredicate<T, I> predicate) {
        this.predicate = predicate;
    }

    @SafeVarargs
    public static <I, T> ImportCondition<T, I> allOf(ImportCondition<T, I>... criteria) {
        return new ImportCondition<>((s, i) -> Arrays.stream(criteria).allMatch(c -> c.predicate.test(s, i)));
    }
    public static <I, T> ImportCondition<T, I> always() {
        return new ImportCondition<>((s, i) -> true);
    }

    public static <T, I> ImportCondition<T, I> index(I index) {
        return new ImportCondition<>((s, i) -> i.equals(index));
    }

    public static <T, I> ImportCondition<T, I> probability(double p) {
        return new ImportCondition<>((s, i) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <T, I> ImportCondition<T, I> sourceMatches(Predicate<T> sourceCondition) {
        return new ImportCondition<>((s, i) -> sourceCondition.test(s));
    }

    public static ImportCondition<TileMap.Tile<Character>, Character> allA() {
        //TODO context.lastMatchFailed();
        return null;
    }

    boolean matches(T source, I index) {//TODO import context
        return predicate.test(source, index);
    }
}
