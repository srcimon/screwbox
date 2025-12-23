package dev.screwbox.core.environment.importing;

import dev.screwbox.core.utils.TileMap;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class RuleAppliesIf<T, I> {

    private BiPredicate<T, I> predicate;
    private ContextAwareBlueprint<T> blueprint;

    private RuleAppliesIf(BiPredicate<T, I> predicate) {
        this.predicate = predicate;
    }

    @SafeVarargs
    public static <I, T> RuleAppliesIf<T, I> allOf(RuleAppliesIf<T, I>... criteria) {
        return new RuleAppliesIf<>((s, i) -> Arrays.stream(criteria).allMatch(c -> c.predicate.test(s, i)));
    }
    public static <I, T> RuleAppliesIf<T, I> always() {
        return new RuleAppliesIf<>((s, i) -> true);
    }

    public static <T, I> RuleAppliesIf<T, I> index(I index) {
        return new RuleAppliesIf<>((s, i) -> i.equals(index));
    }

    public static <T, I> RuleAppliesIf<T, I> probability(double p) {
        return new RuleAppliesIf<>((s, i) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <T, I> RuleAppliesIf<T, I> sourceMatches(Predicate<T> sourceCondition) {
        return new RuleAppliesIf<>((s, i) -> sourceCondition.test(s));
    }

    public static RuleAppliesIf<TileMap.Tile<Character>, Character> lastFailed() {
        //TODO context.lastMatchFailed();
        return null;
    }

    boolean matches(T source, I index) {//TODO import context
        return predicate.test(source, index);
    }
}
