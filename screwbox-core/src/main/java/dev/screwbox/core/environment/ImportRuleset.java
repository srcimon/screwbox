package dev.screwbox.core.environment;

public class ImportRuleset<T, I> {

    public ImportRuleset<T, I> test(T t) {
        return this;
    }

    public ImportRuleset<T, I> assign(Condition<T, I> condition, Blueprint<T> blueprint) {
        return this;
    }

    public ImportRuleset<T, I> assign(I t, ComplexBlueprint<T> blueprint) {
        return this;
    }

    public ImportRuleset<T, I> assign(I t, Blueprint<T> blueprint) {
        return this;
    }
}
