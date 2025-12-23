package dev.screwbox.core.environment.population;

import dev.screwbox.core.utils.TileMap;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MatchCriteria<T, I> {

    private BiPredicate<T, I> predicate;
    private EntityBlueprint<T> blueprint;

    private MatchCriteria(BiPredicate<T, I> predicate) {
        this.predicate = predicate;
    }

    public static <I, T> MatchCriteria<T, I> always() {
        return new MatchCriteria<>((s, i) -> true);
    }

    public MatchCriteria assign(EntityBlueprint<T> blueprint) {
        this.blueprint = blueprint;
        return this;
    }

    public static <T, I> MatchCriteria<T, I> matchIndex(I index) {
        return new MatchCriteria<>((s, i) -> i.equals(index));
    }

    public static <T, I> MatchCriteria<T, I> matchProbability(double p) {
        return new MatchCriteria<>((s, i) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <T, I> MatchCriteria<T, I> matchSource(Predicate<T> sourceCondition) {
        return new MatchCriteria<>((s, i) -> sourceCondition.test(s));
    }

    public static MatchCriteria<TileMap.Tile<Character>, Character> lastMatchFailed() {
        //TODO context.lastMatchFailed();
        return null;
    }

    public boolean matches(T source, I index) {//TODO import context
        return predicate.test(source, index);
    }
}
