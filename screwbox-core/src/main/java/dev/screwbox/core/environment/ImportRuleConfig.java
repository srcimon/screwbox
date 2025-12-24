package dev.screwbox.core.environment;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ImportRuleConfig<T, I> {

    public ImportRuleConfig<T, I> test(T t) {
        return this;
    }

    public ImportRuleConfig<T, I> assign(I t, Blueprint<T> blueprint) {
        return this;
    }
}
