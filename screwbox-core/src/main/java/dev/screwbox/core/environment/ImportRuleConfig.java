package dev.screwbox.core.environment;

public class ImportRuleConfig<T, I> {

    public ImportRuleConfig<T, I> test(T t) {
        return this;
    }

    public ImportRuleConfig<T, I> assign(Condition<T, I> condition, Blueprint<T> blueprint) {
        return this;
    }

    public ImportRuleConfig<T, I> assign(I t, ComplexBlueprint<T> blueprint) {
        return this;
    }

    public ImportRuleConfig<T, I> assign(I t, Blueprint<T> blueprint) {
        return this;
    }
}
