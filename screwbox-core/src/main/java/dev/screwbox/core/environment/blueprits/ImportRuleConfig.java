package dev.screwbox.core.environment.blueprits;

import dev.screwbox.core.environment.Condition;

public class ImportRuleConfig<T, I> {

    public ImportRuleConfig<T, I> test(T t) {
        return this;
    }

    public ImportRuleConfig<T, I> assign(Condition<T, I> condition, Blueprint<T> blueprint) {
        return this;
    }

    public ImportRuleConfig<T, I> verify(Condition<T, I> condition, String message) {
        return this;
    }

    public ImportRuleConfig<T, I> assign(I t, Blueprint<T> blueprint) {
        return this;
    }
}
