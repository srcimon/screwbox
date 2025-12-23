package dev.screwbox.core.environment.population;

import dev.screwbox.core.utils.TileMap;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class RuleCriteria<T, I> {

    private BiPredicate<T, I> predicate;
    private EntityBlueprint<T> blueprint;

    private RuleCriteria(BiPredicate<T, I> predicate) {
        this.predicate = predicate;
    }

    @SafeVarargs
    public static <I, T> RuleCriteria<T, I> allOf(RuleCriteria<T, I>... criteria) {
        return new RuleCriteria<>((s, i) -> Arrays.stream(criteria).allMatch(c -> c.predicate.test(s, i)));
    }
    public static <I, T> RuleCriteria<T, I> always() {
        return new RuleCriteria<>((s, i) -> true);
    }

    public static <T, I> RuleCriteria<T, I> matchIndex(I index) {
        return new RuleCriteria<>((s, i) -> i.equals(index));
    }

    public static <T, I> RuleCriteria<T, I> matchProbability(double p) {
        return new RuleCriteria<>((s, i) -> new Random().nextDouble(0, p) < 1);//TODO quark
    }

    public static <T, I> RuleCriteria<T, I> matchSource(Predicate<T> sourceCondition) {
        return new RuleCriteria<>((s, i) -> sourceCondition.test(s));
    }

    public static RuleCriteria<TileMap.Tile<Character>, Character> lastRuleDidNotApply() {
        //TODO context.lastMatchFailed();
        return null;
    }

    boolean matches(T source, I index) {//TODO import context
        return predicate.test(source, index);
    }
}
